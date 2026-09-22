package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // Intercept all methods in this package
    @Around("execution(* com.log.logging.*.*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        // Get method name
        String methodName = joinPoint.getSignature().getName();

        // Store start time
        long start = System.currentTimeMillis();

        // Log method start
        log.info("Started: {}", methodName);

        // Execute original method
        Object result = joinPoint.proceed();

        // Log method end and execution time
        log.info("Finished: {} in {} ms",
                methodName,
                System.currentTimeMillis() - start);

        // Return original result
        return result;
    }

    
}
