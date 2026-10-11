package com.dingdingcatering.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单确认数据传输对象
 */
@Data
public class OrdersConfirmDTO implements Serializable {

    /** 订单ID */
    private Long id;

    /** 订单状态：1-待付款，2-待接单，3-已接单，4-派送中，5-已完成，6-已取货，7-退款 */
    private Integer status;

}
