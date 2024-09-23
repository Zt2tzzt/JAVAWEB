package com.kkcf.controller;

import com.kkcf.pojo.Result;
import com.kkcf.utils.AliyunOSSUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {
    @PostMapping
    public Result<Null> upload(MultipartFile image) throws Exception {
        log.info("image:{}", image);

        // 获取原始文件名
        String originalFilename = image.getOriginalFilename();
        log.info("原始文件名:{}", originalFilename);

        // 将文件存储在服务器的磁盘目录中
        assert originalFilename != null;
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".")); // 获取文件后缀名
        String fileName = UUID.randomUUID() + extName; // 获取文件全名
        log.info("服务器文件名:{}", fileName);

        InputStream is = image.getInputStream();

        AliyunOSSUtil.uploadFileToAliOSS(fileName, is);

        return Result.success();
    }
}
