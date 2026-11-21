package com.dingdingcatering.websocket;

/**
 * WebSocket 连接身份
 * <p>
 * 握手阶段解析 JWT 令牌得到的可信身份，服务端据此区分角色并生成会话标识，
 * 不再依赖客户端自行声明的 sid 前缀。
 *
 * @param role        角色，取值见 {@link WebSocketServer#ROLE_ADMIN} / {@link WebSocketServer#ROLE_USER}
 * @param principalId 主体主键，管理端为员工ID，用户端为用户ID
 * @param name        主体名称，管理端为员工姓名，用户端为空字符串
 */
public record WsIdentity(String role, Long principalId, String name) {
}