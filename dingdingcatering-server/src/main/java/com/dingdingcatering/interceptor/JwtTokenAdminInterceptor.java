package com.dingdingcatering.interceptor;

import com.dingdingcatering.constant.JwtClaimsConstant;
import com.dingdingcatering.constant.MessageConstant;
import com.dingdingcatering.context.BaseContext;
import com.dingdingcatering.exception.TokenExpiredException;
import com.dingdingcatering.exception.TokenInvalidException;
import com.dingdingcatering.exception.UserNotLoginException;
import com.dingdingcatering.properties.JwtProperties;
import com.dingdingcatering.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 管理端JWT令牌校验拦截器
 * <p>
 * 职责：
 * 1. 从请求Header中提取JWT令牌
 * 2. 解析令牌获取员工ID并存入ThreadLocal
 * 3. 请求完成后清理ThreadLocal防止内存泄漏
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {
    private final JwtProperties jwtProperties;

    public JwtTokenAdminInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * 请求处理前校验JWT令牌。
     * <p>
     * 处理流程：
     * 1. 检查请求是否为HandlerMethod
     * 2. 从Header提取令牌
     * 3. 解析令牌获取员工ID
     * 4. 将员工ID存入ThreadLocal
     *
     * @param request  HTTP请求
     * @param response HTTP响应
     * @param handler  处理器
     * @return 是否放行
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader(jwtProperties.getAdminTokenName());
        if (token == null) {
            throw new UserNotLoginException(MessageConstant.USER_NOT_LOGIN);
        }

        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            BaseContext.setCurrentId(empId);
            return true;
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException(ex.getMessage());
        } catch (Exception ex) {
            throw new TokenInvalidException(ex.getMessage());
        }
    }

    /**
     * 请求完成后清理ThreadLocal。
     *
     * @param request  HTTP请求
     * @param response HTTP响应
     * @param handler  处理器
     * @param ex       异常信息
     */
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        BaseContext.removeCurrentId();
    }
}
