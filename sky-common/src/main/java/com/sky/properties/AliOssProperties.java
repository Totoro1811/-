package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用于保存阿里云OSS对象存储相关字段的Bean
 * 会在项目启动的时候自动将application.yml中sky.alioss的信息自动注入到对象中并放到IOC容器中
 */
@Data
@Component
@ConfigurationProperties(prefix = "sky.alioss")
public class AliOssProperties {
    private String endpoint; //桶的地域URL路径
    private String accessKeyId; //阿里云OSS的秘钥
    private String accessKeySecret; //阿里云OSS的秘钥密码
    private String bucketName; //桶名称
}
