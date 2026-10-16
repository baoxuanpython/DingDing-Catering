package com.dingdingcatering.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜品口味关系实体类（对应数据库表：dish_flavor）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishFlavor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 菜品ID */
    private Long dishId;

    /** 口味类型ID（关联 flavor 表） */
    private Long flavorId;

    /** 口味类型名称（冗余字段） */
    private String flavorName;

    /** 口味值列表（冗余字段，逗号分隔） */
    private String valueList;
}
