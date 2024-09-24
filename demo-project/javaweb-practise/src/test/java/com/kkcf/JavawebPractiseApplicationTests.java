package com.kkcf;

import com.kkcf.utils.AliyunOSSProperties1;
import com.kkcf.utils.AliyunOSSProperties2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class JavawebPractiseApplicationTests {
    @Autowired
    private AliyunOSSProperties1 aliyunOSSProperties1;
    @Autowired
    private AliyunOSSProperties2 aliyunOSSProperties2;

    @Test
    void contextLoads() {
        String endpoint1 = aliyunOSSProperties1.getEndpoint();
        log.info("endpoint1: {}", endpoint1);

        String endpoint2 = aliyunOSSProperties2.getEndpoint();
        log.info("endpoint2: {}", endpoint2);
    }
}
