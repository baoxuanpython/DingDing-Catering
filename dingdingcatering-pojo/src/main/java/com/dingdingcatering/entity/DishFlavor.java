package com.dingdingcatering.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜品口味实体类（对应数据库表：dish_flavor）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishFlavor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 口味主键 */
    private Long id;

    /** 菜品ID */
    private Long dishId;

    /** 口味ID */
    private Integer flavorId;

    /** 口味数据值 */
    private String flavorVal;

}
