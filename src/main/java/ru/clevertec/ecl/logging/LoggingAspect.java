package ru.clevertec.ecl.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@annotation(ru.clevertec.ecl.logging.Logging)")
    private void annotationPointcut() {
    }

    @Pointcut("@within(ru.clevertec.ecl.logging.Logging)")
    private void loggingByType() {
    }

    @Around("annotationPointcut() || loggingByType()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        Object methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Before: class::{}", className);
        log.info("Before: method::{}", methodName);
        log.info("Before: args::{}", args);

        Object result = joinPoint.proceed();

        log.info("After: result::{}", result);
        return result;
    }
}
