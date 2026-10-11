package com.dingdingcatering.config;

import com.dingdingcatering.interceptor.JwtTokenUserInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dingdingcatering.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
