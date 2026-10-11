package com.dingdingcatering.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细实体类（对应数据库表：order_detail）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 明细主键 */
    private Long id;

    /** 商品名称 */
    private String name;

    /** 订单ID */
    private Long orderId;

    /** 菜品ID */
    private Long dishId;

    /** 套餐ID */
    private Long setmealId;

    /** 口味信息 */
    private String dishFlavor;

    /** 数量 */
    private Integer number;

    /** 金额 */
    private BigDecimal amount;

    /** 图片URL */
    private String image;
}
