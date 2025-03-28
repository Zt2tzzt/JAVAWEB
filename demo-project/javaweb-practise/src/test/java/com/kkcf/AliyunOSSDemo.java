package com.kkcf;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AliyunOSSDemo {

    public static CredentialsProvider getCredential() throws IOException {
        Properties properties = new Properties();
        // 设置 config.ini 文件路径
        String configFilePath = "config.ini";
        InputStream is = AliyunOSSDemo.class.getClassLoader().getResourceAsStream(configFilePath);

        // 读取配置文件
        //FileInputStream input = new FileInputStream(configFilePath);
        properties.load(is);
        assert is != null;
        is.close();

        // 从配置文件中获取AK和SK
        String accessKeyId = properties.getProperty("alibaba_cloud_access_key_id");
        String accessKeySecret = properties.getProperty("alibaba_cloud_access_key_secret");
        System.out.println("accessKeyId: " + accessKeyId);
        System.out.println("accessKeySecret: " + accessKeySecret);

        return new DefaultCredentialProvider(accessKeyId, accessKeySecret);
    }

    public static void main(String[] args) throws Exception {
        // 不同 Region endpoint 不同
        String endpoint = "https://oss-cn-shenzhen.aliyuncs.com";

        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量 OSS_ACCESS_KEY_ID 和 OSS_ACCESS_KEY_SECRET。
        CredentialsProvider credentialsProvider = getCredential();

        // 填写 Bucket 名称，例如 examplebucket。
        String bucketName = "zetian-bucket";

        // 填写 Object 完整路径，例如 exampledir/exampleobject.txt。Object 完整路径中不能包含 Bucket 名称。
        String objectName = "baja(2).jpg";

        // 创建 OSSClient 实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);

        try {
            ossClient.putObject(bucketName, objectName, new FileInputStream("C:\\Users\\00015167\\Downloads\\baja(1).jpg"));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}