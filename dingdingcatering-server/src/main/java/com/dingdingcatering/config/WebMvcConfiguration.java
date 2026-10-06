package com.dingdingcatering.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.dingdingcatering.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    public WebMvcConfiguration(JwtTokenAdminInterceptor jwtTokenAdminInterceptor) {
        this.jwtTokenAdminInterceptor = jwtTokenAdminInterceptor;
        log.info("WebMvcConfiguration 初始化完成，JWT 拦截器已注入");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");

        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")                          // 拦截所有管理端接口
                .excludePathPatterns("/admin/employee/login");         // 排除登录接口

        log.info("自定义拦截器注册完成 - 拦截路径: /admin/**, 排除路径: /admin/employee/login");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器，统一日期格式化...");


        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();


        ObjectMapper objectMapper = new ObjectMapper();


        JavaTimeModule javaTimeModule = new JavaTimeModule();


        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));


        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));


        objectMapper.registerModule(javaTimeModule);


        converter.setObjectMapper(objectMapper);


        converters.add(0, converter);

        log.info("消息转换器扩展完成 - 已注册自定义 Jackson 转换器，日期格式: yyyy-MM-dd HH:mm:ss");
    }
}
