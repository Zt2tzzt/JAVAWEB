package com.aliyun.oss;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class AliyunOSSUtil2 {
    private AliyunOSSProperties2 aliOSSProperties;

    public AliyunOSSProperties2 getAliOSSProperties() {
        return aliOSSProperties;
    }

    public void setAliOSSProperties(AliyunOSSProperties2 aliOSSProperties) {
        this.aliOSSProperties = aliOSSProperties;
    }

    /**
     * 实现上传图片到OSS
     */
    public String upload(MultipartFile multipartFile) throws IOException {
        // 获取上传的文件的输入流
        InputStream inputStream = multipartFile.getInputStream();

        // 避免文件覆盖
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));

        // 上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(aliOSSProperties.getEndpoint(),
                aliOSSProperties.getAccessKeyId(), aliOSSProperties.getAccessKeySecret());
        ossClient.putObject(aliOSSProperties.getBucketName(), fileName, inputStream);

        // 文件访问路径
        String url = aliOSSProperties.getEndpoint().split("//")[0] + "//" + aliOSSProperties.getBucketName() + "." + aliOSSProperties.getEndpoint().split("//")[1] + "/" + fileName;

        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }
}
