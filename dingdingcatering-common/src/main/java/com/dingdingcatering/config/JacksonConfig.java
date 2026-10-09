package com.dingdingcatering.config;

import com.dingdingcatering.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
@Slf4j
public class JacksonConfig {

    @Bean
    @Primary
    public JacksonObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        JacksonObjectMapper mapper = new JacksonObjectMapper();
        log.info("JacksonObjectMapper已创建 - 类型: {}", mapper.getClass().getName());
        return mapper;
    }
}
