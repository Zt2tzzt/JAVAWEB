package com.kkcf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(2)
//@Aspect
public class MyAspectA {
    @Before("com.kkcf.aop.MyAspect.pt()")
    public void before() {
        log.info("MyAspectA.before……");
    }

    @After("com.kkcf.aop.MyAspect.pt()")
    public void after() {
        log.info("MyAspectA.after……");
    }
}
