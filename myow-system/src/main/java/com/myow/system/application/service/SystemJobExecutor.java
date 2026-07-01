package com.myow.system.application.service;

import com.myow.system.infrastructure.persistence.po.JobDO;
import com.myow.system.infrastructure.persistence.po.JobLogDO;
import com.myow.system.infrastructure.persistence.repository.JobLogRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class SystemJobExecutor {

    private final ApplicationContext applicationContext;
    private final JobLogRepository jobLogRepository;

    public SystemJobExecutor(ApplicationContext applicationContext, JobLogRepository jobLogRepository) {
        this.applicationContext = applicationContext;
        this.jobLogRepository = jobLogRepository;
    }

    public void executeOnce(JobDO job) {
        LocalDateTime start = LocalDateTime.now();
        JobLogDO log = new JobLogDO()
                .setJobId(job.getJobId())
                .setJobName(job.getJobName())
                .setJobGroup(job.getJobGroup())
                .setStartTime(start);
        try {
            invoke(job.getHandlerName());
            LocalDateTime end = LocalDateTime.now();
            log.setEndTime(end)
                    .setCostTime(Duration.between(start, end).toMillis())
                    .setStatus("SUCCESS");
        } catch (Exception ex) {
            LocalDateTime end = LocalDateTime.now();
            log.setEndTime(end)
                    .setCostTime(Duration.between(start, end).toMillis())
                    .setStatus("FAIL")
                    .setErrorMsg(ex.getMessage());
        }
        jobLogRepository.save(log);
    }

    private void invoke(String handlerName) {
        if (handlerName == null || !handlerName.contains(".")) {
            throw new IllegalArgumentException("handlerName must be beanName.methodName");
        }
        String beanName = handlerName.substring(0, handlerName.lastIndexOf('.'));
        String methodName = handlerName.substring(handlerName.lastIndexOf('.') + 1);
        Object bean = applicationContext.getBean(beanName);
        Method method = ReflectionUtils.findMethod(bean.getClass(), methodName);
        if (method == null || method.getParameterCount() != 0) {
            throw new IllegalArgumentException("job handler method must exist and have no arguments");
        }
        ReflectionUtils.makeAccessible(method);
        ReflectionUtils.invokeMethod(method, bean);
    }
}
