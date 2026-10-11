package com.dingdingcatering.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 订单支付数据传输对象
 */
@Data
public class OrdersPaymentDTO implements Serializable {

    /** 订单号 */
    private String orderNumber;

    /** 付款方式：1-微信，2-支付宝 */
    private Integer payMethod;

}
