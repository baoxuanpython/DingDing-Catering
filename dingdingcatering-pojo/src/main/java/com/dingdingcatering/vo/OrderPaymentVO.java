package com.dingdingcatering.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单支付视图对象（用于微信支付调起）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentVO implements Serializable {

    /** 随机字符串 */
    private String nonceStr;

    /** 签名 */
    private String paySign;

    /** 时间戳 */
    private String timeStamp;

    /** 签名算法 */
    private String signType;

    /** 统一下单接口返回的prepay_id参数值 */
    private String packageStr;

}
