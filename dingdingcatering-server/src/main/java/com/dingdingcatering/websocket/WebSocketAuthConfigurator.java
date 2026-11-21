package com.dingdingcatering.websocket;

import jakarta.websocket.server.ServerEndpointConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * WebSocket 握手配置器
 * <p>
 * 负责两件事：
 * <ol>
 *   <li>来源校验：拒绝不在白名单的浏览器来源，防御跨站 WebSocket 劫持</li>
 *   <li>子协议协商：把客户端传入的 {@code ws-token.<jwt>} 原样回显，
 *       使令牌可以通过子协议通道传递（浏览器无法自定义请求头）</li>
 * </ol>
 * 令牌本身的解析与身份绑定在 {@link WebSocketServer#onOpen} 中完成。
 */
@Slf4j
public class WebSocketAuthConfigurator extends ServerEndpointConfig.Configurator {

    /**
     * 来源校验，返回 false 时 Tomcat 直接以 403 拒绝握手
     *
     * @param originHeaderValue 请求头 Origin 的值
     * @return true 表示允许握手
     */
    @Override
    public boolean checkOrigin(String originHeaderValue) {
        boolean allowed = WebSocketAuthSupport.isOriginAllowed(originHeaderValue);
        if (!allowed) {
            log.warn("WebSocket 握手被拒绝，来源不在白名单: origin={}", originHeaderValue);
        }
        return allowed;
    }

    /**
     * 子协议协商
     * <p>
     * 客户端把令牌放在子协议位置（{@code ws-token.<jwt>}），这里原样回显第一个候选值，
     * 连接建立后可通过 {@code session.getNegotiatedSubprotocol()} 取回。
     *
     * @param supported 服务端声明的子协议
     * @param requested 客户端请求的子协议
     * @return 协商结果
     */
    @Override
    public String getNegotiatedSubprotocol(List<String> supported, List<String> requested) {
        if (requested != null && !requested.isEmpty()) {
            return requested.get(0);
        }
        return "";
    }
}