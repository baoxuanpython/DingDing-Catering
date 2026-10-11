package com.dingdingcatering.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单拒绝数据传输对象
 */
@Data
public class OrdersRejectionDTO implements Serializable {

    /** 订单ID */
    private Long id;

    /** 订单拒绝原因 */
    private String rejectionReason;

}
