package com.dingdingcatering.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 订单统计视图对象
 */
@Data
public class OrderStatisticsVO implements Serializable {

    /** 待接单数 */
    private Integer toBeConfirmed;

    /** 待派送数 */
    private Integer confirmed;

    /** 派送中数量 */
    private Integer deliveryInProgress;

    /** 已完成数量 */
    private Integer completed;

    /** 已取消数量 */
    private Integer cancelled;

    /** 已退款数量 */
    private Integer refunded;
}
