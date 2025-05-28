package com.schedule.calendar.Aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.schedule.calendar.Controllers.*.*(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logBeforeControllerMethod(JoinPoint joinPoint) {
        logger.info("Executing {} with arguments {}", 
            joinPoint.getSignature().getName(), 
            joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterControllerMethod(JoinPoint joinPoint, Object result) {
        logger.info("Method {} completed successfully", joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "error")
    public void logError(JoinPoint joinPoint, Throwable error) {
        logger.error("Error in {}.{}() with cause = {}", 
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            error.getCause() != null ? error.getCause() : "NULL");
    }
}