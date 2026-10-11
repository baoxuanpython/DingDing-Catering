package com.dingdingcatering.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单提交结果视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSubmitVO implements Serializable {

    /** 订单主键 */
    private Long id;

    /** 订单号 */
    private String orderNumber;

    /** 订单金额 */
    private BigDecimal orderAmount;

    /** 下单时间 */
    private LocalDateTime orderTime;
}
