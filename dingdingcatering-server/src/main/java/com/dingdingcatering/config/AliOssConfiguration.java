package com.dingdingcatering.config;

import com.dingdingcatering.properties.AliOssProperties;
import com.dingdingcatering.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AliOssConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("阿里云文件上传工具类对象已创建 - 上传配置：{}", aliOssProperties);
        return new AliOssUtil(aliOssProperties);
    }
}
