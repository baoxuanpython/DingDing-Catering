package com.dingdingcatering.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单取消数据传输对象
 */
@Data
public class OrdersCancelDTO implements Serializable {

    /** 订单ID */
    private Long id;

    /** 订单取消原因 */
    private String cancelReason;

}
