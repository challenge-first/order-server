package com.example.orderserver.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

// 로깅 AOP
@Slf4j
@Component
@Aspect
public class LogAspect {

    @Pointcut("execution(* com.example.orderserver.order..*(..))")
    public void all() {}

    @Before("all()")
    public void logAll(JoinPoint joinPoint) {

        log.info("[{}]" , joinPoint.getSignature().getName());
    }
}
