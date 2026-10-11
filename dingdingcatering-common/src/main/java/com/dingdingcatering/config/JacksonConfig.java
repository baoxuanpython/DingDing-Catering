package com.dingdingcatering.config;

import com.dingdingcatering.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Jackson 序列化配置类
 * <p>
 * 注册自定义的 JacksonObjectMapper，替代默认的 ObjectMapper，
 * 使 Jackson 能够正确处理 Java 8 时间类型（LocalDate、LocalTime、LocalDateTime）的序列化与反序列化。
 */
@Configuration
@Slf4j
public class JacksonConfig {

    /**
     * 创建自定义 JacksonObjectMapper 并注册为 Spring 容器的主 Bean
     *
     * @return 配置好的 JacksonObjectMapper 实例
     */
    @Bean
    @Primary
    public JacksonObjectMapper jacksonObjectMapper() {
        JacksonObjectMapper mapper = new JacksonObjectMapper();
        log.info("JacksonObjectMapper已创建 - 类型: {}", mapper.getClass().getName());
        return mapper;
    }
}
