package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: AliOssConfiguration
 * USER: SHINIAN
 * DATE: 2023/5/17
 * DESCRIPTION : 阿里云Oss上传的工具类的注入类
 */
@Slf4j
@Configuration
public class AliOssConfiguration {

    /**
     * 将阿里云OssUtil注入到容器中
     * 当注入第三方Bean并且需要当前容器中的一个Bean的时候,可以将对应的类型的Bean声明为方法的形式参数,在扫描的时候会自动尝试从容器中获取该类型的Bean作为参数传递!
     *
     * @return 阿里云Oss上传工具类
     */
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("【开始】 注入阿里云Oss上传工具类");
        AliOssUtil aliOssUtil = new AliOssUtil(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(), aliOssProperties.getBucketName());
        return aliOssUtil;
    }
}
