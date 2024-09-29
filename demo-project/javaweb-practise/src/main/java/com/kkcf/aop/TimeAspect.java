package com.kkcf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
//@Aspect
public class TimeAspect {
    @Around("execution(* com.kkcf.service.*.*(..))") // 切入点表达式。
    public Object recordTime(ProceedingJoinPoint pjp) throws Throwable {
        // 1.记录开始时间
        long start = System.currentTimeMillis();

        // 2.执行目标方法
        Object result = pjp.proceed();

        // 3.记录结束时间
        long end = System.currentTimeMillis();
        log.info("{} 方法耗时：{} ms", pjp.getSignature(), end - start);

        return result;
    }
}
