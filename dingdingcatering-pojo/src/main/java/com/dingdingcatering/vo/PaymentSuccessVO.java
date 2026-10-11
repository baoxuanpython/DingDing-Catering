package com.dingdingcatering.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付成功视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessVO {

    /** 预计送达时间 */
    private String estimatedDeliveryTime;
}
