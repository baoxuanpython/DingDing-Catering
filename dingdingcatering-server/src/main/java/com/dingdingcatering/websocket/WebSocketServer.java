package com.dingdingcatering.websocket;

import com.alibaba.fastjson2.JSON;
import com.dingdingcatering.exception.BaseException;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket服务
 * <p>
 * 连接流程：
 * <ol>
 *   <li>握手阶段由 {@link WebSocketAuthConfigurator} 校验 Origin 白名单，并回显令牌子协议</li>
 *   <li>{@link #onOpen} 解析令牌，校验失败立即以 1008（策略违规）关闭连接，不登记会话</li>
 *   <li>校验通过后由服务端生成会话标识，客户端传入的 sid 仅用于日志追踪</li>
 * </ol>
 */
@Component
@Slf4j
@ServerEndpoint(value = "/ws/{sid}", configurator = WebSocketAuthConfigurator.class)
public class WebSocketServer {

    /**
     * 管理端角色标识
     */
    public static final String ROLE_ADMIN = "admin";

    /**
     * 用户端角色标识
     */
    public static final String ROLE_USER = "user";

    /**
     * 已认证会话
     * <p>
     * 以 Session 实例本身为键：Session 与连接一一对应，
     * 因此不会出现客户端自定义 sid 相同导致互相覆盖（顶号）的问题。
     */
    private static final Map<Session, SessionInfo> SESSIONS = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     * <p>
     * 令牌优先从协商子协议中取（浏览器无法自定义请求头），
     * 取不到时回退到 URL 查询参数 token，兼容脚本调试。
     *
     * @param session 会话
     * @param sid     客户端传入的标识，仅用于日志追踪
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        String token = resolveToken(session);
        WsIdentity identity;
        try {
            identity = WebSocketAuthSupport.authenticate(token);
        } catch (BaseException e) {
            log.warn("WebSocket 连接认证失败，关闭连接: clientSid={}, code={}, reason={}",
                    sid, e.getErrorCode().getCode(), e.getErrorCode().getMessage());
            closeQuietly(session, e.getErrorCode().getMessage());
            return;
        }

        String serverSid = identity.role() + "-" + identity.principalId() + "-"
                + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        SESSIONS.put(session, new SessionInfo(serverSid, identity));
        log.info("WebSocket 连接建立: clientSid={}, serverSid={}, role={}, principalId={}",
                sid, serverSid, identity.role(), identity.principalId());

        sendConnected(session, serverSid, identity);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param sid     客户端传入的标识
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.debug("收到来自客户端:{}的信息:{}", sid, message);
    }

    /**
     * 连接关闭调用的方法
     *
     * @param session 会话
     * @param sid     客户端传入的标识
     */
    @OnClose
    public void onClose(Session session, @PathParam("sid") String sid) {
        SessionInfo info = SESSIONS.remove(session);
        log.info("WebSocket 连接断开: clientSid={}, serverSid={}",
                sid, info == null ? "已由异常回调清理" : info.serverSid());
    }

    /**
     * 连接异常回调，异常连接直接清理
     *
     * @param session 会话
     * @param error   异常
     */
    @OnError
    public void onError(Session session, Throwable error) {
        if (error.getMessage() == null) {
            log.debug("WebSocket 连接被对端直接断开");
        } else {
            log.warn("WebSocket 连接异常: {}", error.getMessage());
        }
        SESSIONS.remove(session);
        closeQuietly(session, "连接异常");
    }

    /**
     * 向管理端群发消息
     * <p>
     * 仅发送给角色为管理端的已认证会话，小程序用户端不会收到商家的订单类提醒。
     *
     * @param message 消息内容
     */
    public void sendToAdminClient(String message) {
        sendToRole(ROLE_ADMIN, message);
    }

    /**
     * 按角色群发消息，发送失败的会话会被清理出集合
     *
     * @param role    角色
     * @param message 消息内容
     */
    private void sendToRole(String role, String message) {
        for (Map.Entry<Session, SessionInfo> entry : SESSIONS.entrySet()) {
            SessionInfo info = entry.getValue();
            if (!role.equals(info.identity().role())) {
                continue;
            }
            Session session = entry.getKey();
            if (!session.isOpen()) {
                SESSIONS.remove(session);
                continue;
            }
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("发送消息失败，移除失效会话: serverSid={}", info.serverSid(), e);
                SESSIONS.remove(session);
            }
        }
    }

    /**
     * 解析令牌：优先协商子协议，其次 URL 查询参数
     *
     * @param session 会话
     * @return 令牌，未携带返回 null
     */
    private String resolveToken(Session session) {
        String token = WebSocketAuthSupport.tokenFromNegotiatedSubprotocol(session.getNegotiatedSubprotocol());
        if (token != null) {
            return token;
        }
        return WebSocketAuthSupport.tokenFromRequestParameters(session.getRequestParameterMap());
    }

    /**
     * 向客户端下发连接建立回执，告知服务端生成的会话标识
     *
     * @param session   会话
     * @param serverSid 服务端生成的会话标识
     * @param identity  连接身份
     */
    private void sendConnected(Session session, String serverSid, WsIdentity identity) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "connected");
        payload.put("sid", serverSid);
        payload.put("role", identity.role());
        payload.put("principalId", identity.principalId());
        try {
            session.getBasicRemote().sendText(JSON.toJSONString(payload));
        } catch (Exception e) {
            log.error("下发连接回执失败，移除会话: serverSid={}", serverSid, e);
            SESSIONS.remove(session);
        }
    }

    /**
     * 以 1008（策略违规）关闭连接，关闭原因长度受协议限制，需截断
     *
     * @param session 会话
     * @param reason  关闭原因
     */
    private void closeQuietly(Session session, String reason) {
        String text = reason == null ? "认证失败" : reason;
        if (text.length() > 100) {
            text = text.substring(0, 100);
        }
        try {
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, text));
        } catch (Exception e) {
            log.debug("关闭连接失败: {}", e.getMessage());
        }
    }

    /**
     * 会话信息
     *
     * @param serverSid 服务端生成的会话标识
     * @param identity  连接身份
     */
    private record SessionInfo(String serverSid, WsIdentity identity) {
    }
}