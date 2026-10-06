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
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {
    private final JwtProperties jwtProperties;

    public JwtTokenAdminInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

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
}
