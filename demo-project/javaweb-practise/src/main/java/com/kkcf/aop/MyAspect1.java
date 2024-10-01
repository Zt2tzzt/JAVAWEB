package com.kkcf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Component
//@Aspect
public class MyAspect1 {
    @Before("@annotation(com.kkcf.anno.MyLog)")
    public void before() {
        log.info("MyAspect1.before ……");
    }

    @After("@annotation(com.kkcf.anno.MyLog)")
    public void after() {
        log.info("MyAspect1.after ……");
    }
}
