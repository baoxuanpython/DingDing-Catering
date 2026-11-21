package com.dingdingcatering.service.common;

import com.dingdingcatering.vo.MessagePageResult;

/**
 * 消息通知服务
 */
public interface MessageService {

    /**
     * 保存一条消息
     *
     * @param type        消息类型：1-新订单待接单，2-订单催单提醒
     * @param orderId     订单主键
     * @param orderNumber 订单号
     * @param content     消息内容
     */
    void save(Integer type, Long orderId, String orderNumber, String content);

    /**
     * 幂等保存：同一类型 + 同一订单号已存在时不重复写入
     *
     * @param type        消息类型
     * @param orderId     订单主键
     * @param orderNumber 订单号
     * @param content     消息内容
     * @return 是否真正写入
     */
    boolean saveIfAbsent(Integer type, Long orderId, String orderNumber, String content);

    /**
     * 分页查询消息列表（同时返回总数与未读数）
     *
     * @param page     页码，从 1 开始
     * @param pageSize 每页条数，上限 50
     * @return 分页结果
     */
    MessagePageResult pageQuery(Integer page, Integer pageSize);

    /**
     * 将全部未读消息标记为已读
     */
    void markAllRead();

    /**
     * 清理过期消息，保留最近 30 天
     */
    void cleanExpired();
}