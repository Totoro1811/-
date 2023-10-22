package com.sky.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

/**
 * AliOSS上传工具类(▲:当前情况下未注入到IOC中 并没有标记@Component) 需要单独编写Configuration类注入
 */
@Slf4j
@Data
@AllArgsConstructor
public class AliOssUtil {
    private String endpoint; //桶的地域URL路径
    private String accessKeyId; //阿里云OSS的秘钥
    private String accessKeySecret; //阿里云OSS的秘钥密码
    private String bucketName; //桶名称

    /**
     * 基于字节数组完成文件上传
     *
     * @param bytes      要上传的文件的字节数组
     * @param objectName 上传到桶中文件的名称
     * @return 文件上传后的URL访问路径
     */
    public String upload(byte[] bytes, String objectName) {
        //创建OSSClient实例用于完成文件上传
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            //基于putObject方法传递桶名称/桶中文件名称/字节数组输入流完成文件上传
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
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
        //基于文件访问路径规则生成URL https://BucketName.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder.append(bucketName).append(".").append(endpoint).append("/").append(objectName);
        log.info("文件已上传到阿里OSS,访问URL : {}", stringBuilder);
        return stringBuilder.toString();
    }
}
