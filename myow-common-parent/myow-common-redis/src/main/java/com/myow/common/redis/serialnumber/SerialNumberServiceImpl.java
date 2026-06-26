package com.myow.common.redis.serialnumber;


import com.myow.common.exception.SerialNumberException;
import com.myow.common.response.ResultCode;
import com.myow.common.support.serialnumber.SerialContext;
import com.myow.common.support.serialnumber.SerialNumberGenerator;
import com.myow.common.support.serialnumber.SerialNumberService;
import com.myow.common.support.serialnumber.SerialTemplate;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 单号生成核心协调服务（放在 infrastructure 层）
 * 职责：
 * 1. 分布式锁
 * 2. 查找对应类型的生成器
 * 3. 获取上一个单号（由业务实现）
 * 4. 计算下一个序列号
 * 5. 使用业务定义的模板渲染最终单号
 */
@Service
public class SerialNumberServiceImpl implements SerialNumberService {

    private static final int MAX_RETRY = 5;
    private static final long LOCK_WAIT_SECONDS = 3;
    private static final long LOCK_HOLD_SECONDS = 15;

    private final RedissonClient redisson;

    @Resource
    private ApplicationContext applicationContext;

    // 缓存已找到的生成器（按类型缓存，避免每次反射查找）
    private final ConcurrentMap<String, SerialNumberGenerator> generatorCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, ReentrantLock> localLocks = new ConcurrentHashMap<>();

    public SerialNumberServiceImpl(ObjectProvider<RedissonClient> redissonProvider) {
        this.redisson = redissonProvider.getIfAvailable();
    }


    @Override
    public String generate(String type, @Nullable LocalDate businessDate) {
        return generate(type, businessDate, null);
    }

    /**
     * 生成单号主入口
     *
     * @param type         单号类型（ORDER, CONTRACT, USER, PURCHASE 等）
     * @param businessDate 业务日期（用于分段、重置序列等），为空时使用当天
     * @param businessObj  可选：业务对象（如 OrderDTO、UserCreateCmd），供模板中的动态字段使用
     * @return 生成的完整单号
     */
    @Override
    public String generate(String type, @Nullable LocalDate businessDate, Object businessObj) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("单号类型不能为空");
        }

        String upperType = type.trim().toUpperCase();

        // 业务日期默认使用当天
        LocalDate date = businessDate != null ? businessDate : LocalDate.now();

        // 延迟加载 + 缓存生成器
        SerialNumberGenerator generator = generatorCache.computeIfAbsent(upperType, key -> {
            var beans = applicationContext.getBeansOfType(SerialNumberGenerator.class);
            return beans.values().stream()
                    .filter(g -> g.getTemplate().getSupportedTypes().contains(key))
                    .findFirst()
                    .orElseThrow(() -> new SerialNumberException(ResultCode.SERIAL_NUMBER_NOT_FOUND, key));
        });

        // 锁粒度：类型 + 日期（yyyy-MM-dd）
        String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String lockKey = "lock:serial:" + upperType + ":" + dateStr;
        if (redisson == null) {
            return generateWithLocalLock(lockKey, upperType, date, businessObj, generator);
        }
        RLock lock = redisson.getLock(lockKey);

        SerialNumberException lastException = null;

        for (int attempt = 1; attempt <= MAX_RETRY; attempt++) {
            try {
                if (!lock.tryLock(LOCK_WAIT_SECONDS, LOCK_HOLD_SECONDS, TimeUnit.SECONDS)) {
                    if (attempt == MAX_RETRY) {
                        throw new SerialNumberException(
                                ResultCode.SERIAL_NUMBER_LOCK_TIMEOUT, upperType, date);
                    }
                    Thread.sleep(50 * attempt);
                    continue;
                }

                try {
                    // 1. 业务侧自己决定如何获取“上一个完整单号”
                    @Nullable String lastFullNo = generator.getLastNumber(upperType, date);

                    // 2. 从上一个单号中解析出当时的序列值（业务可覆盖默认实现）
                    Long prevSeq = generator.parsePreviousSeq(lastFullNo, generator.getTemplate());

                    // 3. 计算下一个序列号（从 1 开始）
                    long nextSeq = (prevSeq == null ? 0L : prevSeq) + 1;

                    // 4. 构建上下文
                    SerialContext context = SerialContext.builder()
                            .type(upperType)
                            .date(date)
                            .businessObject(businessObj)
                            .currentSeq(nextSeq)
                            .build();

                    // 5. 使用业务定义的模板渲染最终单号
                    SerialTemplate template = generator.getTemplate();
                    return template.render(context);

                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SerialNumberException(ResultCode.SYSTEM_ERROR);
            } catch (Exception e) {
                lastException = new SerialNumberException(ResultCode.SERIAL_NUMBER_GENERATE_FAILED, attempt, e);
                if (attempt == MAX_RETRY) {
                    throw lastException;
                }
                // 指数退避
                try {
                    Thread.sleep(100L * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        throw lastException != null ? lastException : new SerialNumberException(ResultCode.SYSTEM_ERROR);
    }

    private String generateWithLocalLock(String lockKey,
                                         String upperType,
                                         LocalDate date,
                                         Object businessObj,
                                         SerialNumberGenerator generator) {
        ReentrantLock lock = localLocks.computeIfAbsent(lockKey, key -> new ReentrantLock());
        lock.lock();
        try {
            return doGenerate(upperType, date, businessObj, generator);
        } finally {
            lock.unlock();
        }
    }

    private String doGenerate(String upperType,
                              LocalDate date,
                              Object businessObj,
                              SerialNumberGenerator generator) {
        @Nullable String lastFullNo = generator.getLastNumber(upperType, date);
        Long prevSeq = generator.parsePreviousSeq(lastFullNo, generator.getTemplate());
        long nextSeq = (prevSeq == null ? 0L : prevSeq) + 1;

        SerialContext context = SerialContext.builder()
                .type(upperType)
                .date(date)
                .businessObject(businessObj)
                .currentSeq(nextSeq)
                .build();

        SerialTemplate template = generator.getTemplate();
        return template.render(context);
    }

}
