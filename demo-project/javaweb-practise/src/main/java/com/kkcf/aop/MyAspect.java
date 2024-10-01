package com.kkcf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class MyAspect {
    @Pointcut("execution(* com.kkcf.service.impl.DeptServiceImpl.*(..))")
    public void pt() {
    }

    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info(" AOP ADVICE around before...");

        String className = pjp.getTarget().getClass().getName(); // 获取目标类名
        log.info(" className: {}", className);

        Signature signature = pjp.getSignature(); // 获取目标方法签名
        log.info(" signature: {}", signature);

        String methhodName = pjp.getSignature().getName(); // 获取目标方法名
        log.info(" methhodName: {}", methhodName);

        Object[] args = pjp.getArgs(); // 获取目标方法运行参数
        log.info(" args: {}", Arrays.toString(args));

        Object result = pjp.proceed(args); // 执行原始方法，获取返回值。

        log.info(" AOP ADVICE around after...");

        return result;
    }

    @Before("pt()")
    public void before(JoinPoint jp) {
        log.info(" AOP ADVICE before...");
        String className = jp.getTarget().getClass().getName(); // 获取目标类名
        log.info(" className: {}", className);

        Signature signature = jp.getSignature(); // 获取目标方法签名
        log.info(" signature: {}", signature);

        String methodName = jp.getSignature().getName(); // 获取目标方法名
        log.info(" methodName: {}", methodName);

        Object[] args = jp.getArgs(); // 获取目标方法运行参数
        log.info(" args: {}", Arrays.toString(args));
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
