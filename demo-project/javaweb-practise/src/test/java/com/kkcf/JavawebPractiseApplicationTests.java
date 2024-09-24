package com.kkcf;

import com.kkcf.utils.AliyunOSSUtil2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JavawebPractiseApplicationTests {
    //@Autowired
    private AliyunOSSUtil2 aliyunOSSUtil2;

    @Test
    void contextLoads() {
        AliyunOSSUtil2 aliyunOSSUtil2 = new AliyunOSSUtil2();
        System.out.println(aliyunOSSUtil2.bucketName);
    }
}
