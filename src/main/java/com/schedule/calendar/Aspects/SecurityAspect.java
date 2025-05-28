package com.schedule.calendar.Aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityAspect {
    private final Logger logger = LoggerFactory.getLogger(SecurityAspect.class);

    @Pointcut("execution(* com.schedule.calendar.Controllers.*.*(..))")
    public void controllerLayer() {}
    
    @Pointcut("execution(* com.schedule.calendar.Services.*.*(..))")
    public void serviceLayer() {}

    @Before("controllerLayer() || serviceLayer()")
    public void logUserAccess(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            logger.info("User {} accessing {}", 
                authentication.getName(), 
                joinPoint.getSignature().getName());
        }
    }
    
    @Around("com.schedule.calendar.aspects.CommonPointcuts.controllerLayer()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Entering method: {}", methodName);
        try {
            Object result = joinPoint.proceed();
            logger.info("Method {} completed successfully", methodName);
            return result;
        } catch (Exception e) {
            logger.error("Error in method {}: {}", methodName, e.getMessage());
            throw e;
        }
    }
}