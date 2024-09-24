package com.kkcf.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AliyunOSSUtil2 {
    @Value("${aliyun.oos.endpoint}")
    public String endpoint;
    @Value("${aliyun.oos.accessKeyId}")
    public String accessKeyId;
    @Value("${aliyun.oos.accessKeySecret}")
    public String accessKeySecret;
    @Value("${aliyun.oos.bucketName}")
    public String bucketName;
}
