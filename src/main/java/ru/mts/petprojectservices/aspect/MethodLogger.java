package ru.mts.petprojectservices.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class MethodLogger {
    @Around("@annotation(ru.mts.petprojectservices.annotation.Loggable)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            stopWatch.stop();
            log.info("Execution {} ({}) result: {}, in {} ms",
                    joinPoint.getSignature().getName(),
                    joinPoint.getArgs(),
                    result,
                    stopWatch.getTotalTimeMillis());
        }
    }
}
