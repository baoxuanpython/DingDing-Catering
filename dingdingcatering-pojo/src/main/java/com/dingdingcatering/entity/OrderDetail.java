package com.dingdingcatering.entity;

import com.dingdingcatering.vo.DishVO;
import com.dingdingcatering.vo.SetMealVO;
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

    /** 商品名称（下单时快照） */
    private String name;

    /** 图片URL（下单时快照） */
    private String image;

    /** 订单ID */
    private Long orderId;

    /** 菜品ID */
    private Long dishId;

    /** 套餐ID */
    private Long setmealId;

    /** 口味类型ID列表（逗号分隔，用于插入） */
    private String flavorId;

    /** 口味类型名称列表（逗号分隔，用于查询返回，如"辣度,咸度"） */
    private String flavorName;

    /** 指定口味值列表（逗号分隔，如"微辣,少盐"） */
    private String flavorVal;

    /** 菜品数量（单点场景：用户点的该菜品份数；套餐场景：套餐内该菜品份数 × 套餐份数） */
    private Integer dishCopies;

    /** 套餐数量（仅套餐场景有值，表示用户点的套餐份数） */
    private Integer setmealCopies;

    /** 金额（下单时快照） */
    private BigDecimal amount;

    /** 菜品完整信息（仅当dishId不为空时填充） */
    private DishVO dishVO;

    /** 套餐完整信息（仅当setmealId不为空时填充） */
    private SetMealVO setmealVO;
}