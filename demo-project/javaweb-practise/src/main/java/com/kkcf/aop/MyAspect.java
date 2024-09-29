package com.kkcf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
//@Aspect
public class MyAspect {
    @Pointcut("execution(* com.kkcf.service.impl.DeptServiceImpl.*(..))")
    public void pt() {
    }

    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info(" AOP ADVICE around before...");

        Object result = pjp.proceed();

        log.info(" AOP ADVICE around after...");

        return result;
    }

    @Before("pt()")
    public void before() {
        log.info(" AOP ADVICE before...");
    }

    @After("pt()")
    public void after() {
        log.info(" AOP ADVICE after...");
    }

    @AfterReturning("pt()")
    public void afterReturning() {
        log.info(" AOP ADVICE afterReturning...");
    }

    @AfterThrowing("pt()")
    public void afterThrowing() {
        log.info(" AOP ADVICE afterThrowing...");
    }
}
