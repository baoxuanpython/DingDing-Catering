package com.dingdingcatering.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云 OSS 配置属性类
 * <p>
 * 从配置文件中读取 dingding.alioss 前缀的配置项，
 * 用于初始化 OSS 客户端并执行文件上传/删除操作。
 */
@Component
@ConfigurationProperties(prefix = "dingding.alioss")
@Data
public class AliOssProperties {
    /** Bucket 名称 */
    private String bucketName;
    /** OSS 服务区域 */
    private String region;
    /** 访问密钥 ID */
    private String accessKeyId;
    /** 访问密钥 Secret */
    private String accessKeySecret;
}
