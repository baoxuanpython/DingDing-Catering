package com.dingdingcatering.websocket;

import com.dingdingcatering.constant.JwtClaimsConstant;
import com.dingdingcatering.enumeration.ErrorCode;
import com.dingdingcatering.exception.TokenExpiredException;
import com.dingdingcatering.exception.TokenInvalidException;
import com.dingdingcatering.exception.UserNotLoginException;
import com.dingdingcatering.properties.JwtProperties;
import com.dingdingcatering.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * WebSocket 鉴权支撑类
 * <p>
 * WebSocket 端点由 Tomcat 通过反射实例化，无法直接注入 Spring Bean，
 * 因此这里用静态字段承接 Spring 初始化好的配置，供端点和握手配置器调用。
 * <p>
 * 说明：浏览器原生 WebSocket API 不支持自定义请求头，令牌只能通过
 * 子协议（Sec-WebSocket-Protocol）或 URL 查询参数传递。
 */
@Slf4j
@Component
public class WebSocketAuthSupport {

    /**
     * 子协议通道的令牌分隔标记，前端以 {@code ws-token.<jwt>} 形式传令牌，
     * 这样服务端回显的协商子协议与普通子协议名称可区分。
     */
    public static final String SUBPROTOCOL_TOKEN_PREFIX = "ws-token.";

    /**
     * JWT 配置，由 Spring 构造本 Bean 时写入
     */
    private static volatile JwtProperties jwtProperties;

    /**
     * 握手允许的浏览器来源白名单，为空表示不限制（仅建议本地开发使用）
     */
    private static volatile Set<String> allowedOrigins = Collections.emptySet();

    public WebSocketAuthSupport(JwtProperties jwtProperties,
                                @Value("${dingding.websocket.allowed-origins:}") String allowedOrigins) {
        WebSocketAuthSupport.jwtProperties = jwtProperties;
        WebSocketAuthSupport.allowedOrigins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .collect(Collectors.toUnmodifiableSet());
        log.info("WebSocket 鉴权初始化完成，来源白名单: {}", WebSocketAuthSupport.allowedOrigins);
    }

    /**
     * 校验握手来源
     * <p>
     * 浏览器发起 WebSocket 连接时一定携带 Origin 头，校验白名单可防御跨站 WebSocket 劫持；
     * 非浏览器客户端（如脚本、Postman）不带 Origin，这里放行，由令牌校验兜底。
     *
     * @param origin 请求头 Origin 的值
     * @return true 表示允许握手
     */
    public static boolean isOriginAllowed(String origin) {
        if (origin == null || origin.isBlank()) {
            return true;
        }
        if (allowedOrigins.isEmpty()) {
            return true;
        }
        return allowedOrigins.contains(origin);
    }

    /**
     * 解析并校验令牌，返回连接身份
     * <p>
     * 先按管理端密钥解析，失败再按用户端密钥解析，两端令牌互不通用。
     *
     * @param token JWT 令牌
     * @return 连接身份
     * @throws UserNotLoginException  未携带令牌
     * @throws TokenExpiredException  令牌已过期
     * @throws TokenInvalidException  令牌无效
     */
    public static WsIdentity authenticate(String token) {
        if (token == null || token.isBlank()) {
            throw new UserNotLoginException(ErrorCode.USER_NOT_LOGIN);
        }
        JwtProperties properties = jwtProperties;
        if (properties == null) {
            throw new TokenInvalidException(ErrorCode.TOKEN_INVALID, "WebSocket 鉴权未初始化");
        }

        boolean expired = false;
        try {
            Claims claims = JwtUtil.parseJWT(properties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMPID).toString());
            Object empName = claims.get(JwtClaimsConstant.EMPNAME);
            return new WsIdentity(WebSocketServer.ROLE_ADMIN, empId, empName == null ? "" : empName.toString());
        } catch (ExpiredJwtException e) {
            expired = true;
        } catch (Exception ignore) {
            // 非管理端令牌，继续按用户端密钥解析
        }

        try {
            Claims claims = JwtUtil.parseJWT(properties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            return new WsIdentity(WebSocketServer.ROLE_USER, userId, "");
        } catch (ExpiredJwtException e) {
            expired = true;
        } catch (Exception ignore) {
            // 两端密钥都无法解析，统一按令牌无效处理
        }

        if (expired) {
            throw new TokenExpiredException(ErrorCode.TOKEN_EXPIRED);
        }
        throw new TokenInvalidException(ErrorCode.TOKEN_INVALID);
    }

    /**
     * 从子协议头中提取令牌
     * <p>
     * 浏览器传入的子协议形如 {@code ws-token.<jwt>}，逗号分隔表示多个候选。
     *
     * @param subprotocolHeader Sec-WebSocket-Protocol 请求头的原始值
     * @return 令牌，未找到返回 null
     */
    public static String extractTokenFromSubprotocol(String subprotocolHeader) {
        if (subprotocolHeader == null || subprotocolHeader.isBlank()) {
            return null;
        }
        for (String candidate : subprotocolHeader.split(",")) {
            String value = candidate.trim();
            if (value.startsWith(SUBPROTOCOL_TOKEN_PREFIX)) {
                String token = value.substring(SUBPROTOCOL_TOKEN_PREFIX.length()).trim();
                if (!token.isEmpty()) {
                    return token;
                }
            }
        }
        return null;
    }

    /**
     * 判断协商子协议是否为令牌通道
     *
     * @param negotiatedSubprotocol 协商后的子协议
     * @return true 表示该子协议承载令牌
     */
    public static boolean isTokenSubprotocol(String negotiatedSubprotocol) {
        return negotiatedSubprotocol != null && negotiatedSubprotocol.startsWith(SUBPROTOCOL_TOKEN_PREFIX);
    }

    /**
     * 从协商子协议中取出令牌
     *
     * @param negotiatedSubprotocol 协商后的子协议
     * @return 令牌，非令牌通道返回 null
     */
    public static String tokenFromNegotiatedSubprotocol(String negotiatedSubprotocol) {
        if (!isTokenSubprotocol(negotiatedSubprotocol)) {
            return null;
        }
        return negotiatedSubprotocol.substring(SUBPROTOCOL_TOKEN_PREFIX.length()).trim();
    }

    /**
     * 从请求参数中提取令牌（非浏览器客户端与调试场景使用）
     *
     * @param requestParameterMap 握手请求参数
     * @return 令牌，未找到返回 null
     */
    public static String tokenFromRequestParameters(java.util.Map<String, List<String>> requestParameterMap) {
        if (requestParameterMap == null) {
            return null;
        }
        List<String> values = requestParameterMap.get("token");
        if (values == null || values.isEmpty()) {
            return null;
        }
        String token = values.get(0);
        return token == null || token.isBlank() ? null : token.trim();
    }
}