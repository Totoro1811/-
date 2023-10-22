package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用于保存微信支付/小程序相关参数的Bean
 * 会在项目启动的时候自动会application.yml中sky.wechat的信息自动注入到对象中并放到IOC容器中
 */
@Data
@Component
@ConfigurationProperties(prefix = "sky.wechat")
public class WeChatProperties {
    private String appid; //小程序的appid
    private String secret; //小程序的秘钥
    private String mchid; //商户号
    private String mchSerialNo; //商户API证书的证书序列号
    private String privateKeyFilePath; //商户私钥文件
    private String apiV3Key; //证书解密的密钥
    private String weChatPayCertFilePath; //平台证书
    private String notifyUrl; //支付成功的回调地址
    private String refundNotifyUrl; //退款成功的回调地址
}
