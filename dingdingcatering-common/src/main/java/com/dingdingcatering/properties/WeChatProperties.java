package com.dingdingcatering.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序及支付配置属性类
 * <p>
 * 从配置文件中读取 dingding.wechat 前缀的配置项，
 * 用于调用微信登录接口、发起支付请求和处理退款回调。
 */
@Component
@ConfigurationProperties(prefix = "dingding.wechat")
@Data
public class WeChatProperties {

    private String appid; //小程序的appid
    private String secret; //小程序的秘钥
    private String mchid; //商户号
    private String mchSerialNo; //商户API证书的证书序列号
    private String privateKeyFilePath; //商户私钥文件
    private String apiV3Key; //证书解密的密钥
    private String weChatPayCertFilePath; //平台证书
    private String notifyUrl; //支付成功地回调地址
    private String refundNotifyUrl; //退款成功地回调地址

}
