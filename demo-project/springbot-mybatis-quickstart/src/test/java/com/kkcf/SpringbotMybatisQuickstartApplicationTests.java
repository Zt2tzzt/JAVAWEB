package com.kkcf;

import com.kkcf.mapper.UserMapper;
import com.kkcf.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest // Spring Boot 整合单元测试的注解：类中的单元测试方法在运行时，会自动加载 Spring Boot 环菌，并创建 IOC 容器。
class SpringbotMybatisQuickstartApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void testListUser() {
        List<User> users = userMapper.listUser();
        users.stream().forEach(System.out::println);
    }

}
