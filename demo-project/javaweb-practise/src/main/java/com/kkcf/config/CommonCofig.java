package com.kkcf.config;

import com.kkcf.service.DeptService;
import org.dom4j.io.SAXReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonCofig {

    @Bean // 声明第三方 bean 对象，将当前方法的返回值，交给 IOC 容器管理，称为 IOC 容器的 Bean。
    public SAXReader saxReader(DeptService deptService) {
        // Spring 根据 bean 的依赖关系，自动注入 deptService Bean 对象。
        System.out.println(deptService);
        return new SAXReader();
    }
}
