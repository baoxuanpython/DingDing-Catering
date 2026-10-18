package com.dingdingcatering.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 套餐菜品口味关联实体类（对应数据库表：setmeal_dish_flavor）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetMealDishFlavor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 口味关系主键 */
    private Long id;

    /** 关联的setmeal_dish记录ID */
    private Long setmealDishId;

    /** 口味类型ID（关联flavor表） */
    private Long flavorId;

    /** 默认口味值（如"微辣"） */
    private String flavorValue;

    /** 口味类型名称（冗余字段，如"辣度"） */
    private String flavorName;

    /** 口味值可选列表（冗余字段，如"不辣,微辣,中辣,特辣"） */
    private String valueList;
}