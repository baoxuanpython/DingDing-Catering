package com.dingdingcatering.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 套餐菜品关系实体类（对应数据库表：setmeal_dish）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetMealDish implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关系主键
     */
    private Long id;

    /**
     * 套餐ID
     */
    private Long setmealId;

    /**
     * 菜品ID
     */
    private Long dishId;

    /**
     * 菜品名称（冗余字段）
     */
    private String name;

    /**
     * 菜品原价
     */
    private BigDecimal price;

    /**
     * 份数
     */
    private Integer copies;

    /**
     * 口味列表
     */
    private List<Long> flavors;
}
