package com.myow.infrastructure.config;


import com.alibaba.fastjson2.support.spring6.data.redis.GenericFastJsonRedisSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author: yss
 * @date: 2026-01-20 22:41
 * @description: Redis 配置
 */
@Configuration
public class RedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // key 使用 StringRedisSerializer（推荐，兼容性好）
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // value / hashValue 使用 fastjson2 官方 GenericFastJsonRedisSerializer
        // 它内部使用 JSONB（更快、更小）或 JSON，支持自动类型推断
        GenericFastJsonRedisSerializer genericFastJsonRedisSerializer = new GenericFastJsonRedisSerializer();

        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(genericFastJsonRedisSerializer);

        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(genericFastJsonRedisSerializer);

        // 立即初始化（可选，但推荐）
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /**
     * Redis 缓存管理器 (如果项目使用 @Cacheable)
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 创建默认缓存配置
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()))
                .entryTtl(Duration.ofHours(2)); // 可选：全局过期时间, 默认缓存2小时

        // 创建缓存管理器
        RedisCacheManager cacheManager = RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(defaultCacheConfig)
                .transactionAware()  // 支持事务
                .build();

        logger.info("RedisCacheManager 配置完成");
        return cacheManager;
    }
}
