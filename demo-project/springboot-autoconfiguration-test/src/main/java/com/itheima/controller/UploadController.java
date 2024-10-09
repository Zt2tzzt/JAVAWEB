package com.itheima.controller;

import com.aliyun.oss.AliyunOSSUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    @Autowired
    private AliyunOSSUtil2 aliyunOSSUtil2;

    @PostMapping("/upload")
    public String upload(MultipartFile image) throws Exception {
        //上传文件到阿里云 OSS
        return aliyunOSSUtil2.upload(image);
    }
}
