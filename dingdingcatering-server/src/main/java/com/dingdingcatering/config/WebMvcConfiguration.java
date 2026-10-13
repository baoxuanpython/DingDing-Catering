package com.dingdingcatering.config;

import com.dingdingcatering.interceptor.JwtTokenUserInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dingdingcatering.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * Spring MVC 配置类
 * <p>
 * 主要职责：
 * 1. 注册JWT令牌校验拦截器（管理端/用户端）
 * 2. 扩展消息转换器（使用统一的ObjectMapper）
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    private final JwtTokenUserInterceptor jwtTokenUserInterceptor;
    private final ObjectMapper objectMapper;

    public WebMvcConfiguration(JwtTokenAdminInterceptor jwtTokenAdminInterceptor,
                               JwtTokenUserInterceptor jwtTokenUserInterceptor,
                               ObjectMapper objectMapper) {
        this.jwtTokenAdminInterceptor = jwtTokenAdminInterceptor;
        this.jwtTokenUserInterceptor = jwtTokenUserInterceptor;
        this.objectMapper = objectMapper;
    }

    /**
     * 注册自定义拦截器。
     * <p>
     * 管理端拦截：拦截 /admin/**，排除登录接口
     * 用户端拦截：拦截 /user/**，排除登录接口和店铺状态接口
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/shop/status");
        log.info("自定义拦截器已注册 - 管理端拦截: /admin/**, 用户端拦截: /user/**");
    }

    /**
     * 注册日期格式化转换器。
     * <p>
     * 用于处理 GET 请求查询参数中的日期字符串转换，
     * 与 Jackson 的日期格式保持一致。
     *
     * @param registry 格式化注册器
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, LocalDate.class,
                source -> LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        registry.addConverter(String.class, LocalTime.class,
                source -> LocalTime.parse(source, DateTimeFormatter.ofPattern("HH:mm:ss")));
        registry.addConverter(String.class, LocalDateTime.class,
                source -> {
                    if (source.contains(" ")) {
                        return LocalDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }
                    return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
                });
        log.info("日期格式化转换器已注册 - LocalDate: yyyy-MM-dd, LocalTime: HH:mm:ss, LocalDateTime: yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 扩展消息转换器。
     * <p>
     * 使用统一的 ObjectMapper Bean，确保JSON序列化/反序列化行为一致。
     *
     * @param converters 消息转换器列表
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        converters.add(0, converter);
        log.info("消息转换器已扩展 - 使用统一的 ObjectMapper Bean");
    }
}
