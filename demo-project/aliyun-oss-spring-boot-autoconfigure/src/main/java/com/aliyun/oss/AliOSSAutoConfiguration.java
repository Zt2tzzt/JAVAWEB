package com.aliyun.oss;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 声明当前类为 Spring 的配置类
@EnableConfigurationProperties(AliyunOSSProperties2.class) // 导入 AliyunOSSProperties2 类的 Bean 对象，并交给 Spring IOC 容器管理
public class AliOSSAutoConfiguration {
    @Bean
    public AliyunOSSUtil2 aliyunOSSUtil2(AliyunOSSProperties2 aliyunOSSProperties2) {
        AliyunOSSUtil2 aliyunOSSUtil2 = new AliyunOSSUtil2();
        aliyunOSSUtil2.setAliOSSProperties(aliyunOSSProperties2);
        return aliyunOSSUtil2;
    }
}
