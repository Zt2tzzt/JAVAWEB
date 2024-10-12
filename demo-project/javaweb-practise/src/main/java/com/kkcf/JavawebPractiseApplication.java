package com.kkcf;

//import com.example.EnableHeaderConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

//@EnableHeaderConfig // 使用第三方依赖提供的Enable开头的注解
@ServletComponentScan // 当前项目开启了对 JavaWeb（Servlet）组件的支持。
@SpringBootApplication
public class JavawebPractiseApplication {
    public static void main(String[] args) {
        SpringApplication.run(JavawebPractiseApplication.class, args);
    }

    /*@Bean // 声明第三方 bean 对象，将当前方法的返回值，交给 IOC 容器管理，称为 IOC 容器的 Bean。
    public SAXReader saxReader() {
        return new SAXReader();
    }*/
}
