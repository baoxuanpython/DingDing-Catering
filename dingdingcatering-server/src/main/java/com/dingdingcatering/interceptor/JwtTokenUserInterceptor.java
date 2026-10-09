package com.dingdingcatering.interceptor;

import com.dingdingcatering.constant.JwtClaimsConstant;
import com.dingdingcatering.constant.MessageConstant;
import com.dingdingcatering.context.BaseContext;
import com.dingdingcatering.entity.User;
import com.dingdingcatering.exception.TokenExpiredException;
import com.dingdingcatering.exception.TokenInvalidException;
import com.dingdingcatering.exception.UserNotLoginException;
import com.dingdingcatering.mapper.user.UserMapper;
import com.dingdingcatering.properties.JwtProperties;
import com.dingdingcatering.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {
    private final JwtProperties jwtProperties;
    private final UserMapper userMapper;

    public JwtTokenUserInterceptor(JwtProperties jwtProperties, UserMapper userMapper) {
        this.jwtProperties = jwtProperties;
        this.userMapper = userMapper;
    }

    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader(jwtProperties.getUserTokenName());
        if (token == null) {
            throw new UserNotLoginException(MessageConstant.USER_NOT_LOGIN);
        }
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            String openid = claims.get(JwtClaimsConstant.OPENID).toString();

            User user = userMapper.getById(userId, openid);
            if (user == null) {
                log.warn("用户不存在: {}, openid: {}", userId, openid);
                throw new UserNotLoginException(MessageConstant.USER_NOT_FOUND);
            }

            BaseContext.setCurrentId(userId);
            log.debug("用户验证通过: {}, openid={}", userId, user.getOpenid());
            return true;
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException(ex.getMessage());
        } catch (UserNotLoginException | TokenExpiredException | TokenInvalidException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new TokenInvalidException(ex.getMessage());
        }
    }
}
