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

/**
 * C端JWT令牌校验拦截器
 * <p>
 * 职责：
 * 1. 从请求Header中提取JWT令牌
 * 2. 解析令牌获取用户ID和openid
 * 3. 校验用户是否存在于数据库
 * 4. 将用户ID存入ThreadLocal
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {
    private final JwtProperties jwtProperties;
    private final UserMapper userMapper;

    public JwtTokenUserInterceptor(JwtProperties jwtProperties, UserMapper userMapper) {
        this.jwtProperties = jwtProperties;
        this.userMapper = userMapper;
    }

    /**
     * 请求处理前校验JWT令牌。
     * <p>
     * 处理流程：
     * 1. 检查请求是否为HandlerMethod
     * 2. 从Header提取令牌
     * 3. 解析令牌获取用户ID和openid
     * 4. 查询数据库校验用户是否存在
     * 5. 将用户ID存入ThreadLocal
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
