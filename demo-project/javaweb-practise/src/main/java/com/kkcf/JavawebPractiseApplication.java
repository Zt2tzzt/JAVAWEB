package com.kkcf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan // 当前项目开启了对 JavaWeb（Servlet）组件的支持。
@SpringBootApplication
public class JavawebPractiseApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavawebPractiseApplication.class, args);
    }
}
