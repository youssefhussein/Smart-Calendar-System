package com.schedule.calendar.Aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {
    private final Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);

    @Around("execution(* com.schedule.calendar.Controllers.*.*(..))")
    public Object measureMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        logger.info("Method {} executed in {} ms", 
            joinPoint.getSignature().getName(), 
            (end - start));
        return result;
    }
}