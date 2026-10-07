package com.dingdingcatering.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dingding.alioss")
@Data
public class AliOssProperties {
    private String bucketName;
    private String region;
    private String accessKeyId;
    private String accessKeySecret;
}
