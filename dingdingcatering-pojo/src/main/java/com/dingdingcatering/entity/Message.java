package com.dingdingcatering.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息通知实体类（对应数据库表：message）
 * <p>
 * 消息由用户端业务写入（支付成功、催单），由管理端读取展示，
 * 属于两端共享的数据，因此不归属于 user / admin 任一侧。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    /**
     * 消息类型：1-新订单待接单
     */
    public static final Integer TYPE_NEW_ORDER = 1;

    /**
     * 消息类型：2-订单催单提醒
     */
    public static final Integer TYPE_REMINDER = 2;

    /**
     * 已读状态：0-未读
     */
    public static final Integer UNREAD = 0;

    /**
     * 已读状态：1-已读
     */
    public static final Integer READ = 1;

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 消息类型：1-新订单待接单，2-订单催单提醒 */
    private Integer type;

    /** 订单主键 */
    private Long orderId;

    /** 订单号 */
    private String orderNumber;

    /** 消息内容 */
    private String content;

    /** 是否已读：0-未读，1-已读（店铺级共享，任一店员标记即全店已读） */
    private Integer isRead;

    /** 创建时间 */
    private LocalDateTime createTime;
}