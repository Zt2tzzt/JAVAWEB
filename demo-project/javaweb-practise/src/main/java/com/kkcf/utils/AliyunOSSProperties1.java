package com.kkcf.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AliyunOSSProperties1 {
    @Value("${aliyun.oss.endpoint}")
    public String endpoint;
    @Value("${aliyun.oss.accessKeyId}")
    public String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret}")
    public String accessKeySecret;
    @Value("${aliyun.oss.bucketName}")
    public String bucketName;
}
