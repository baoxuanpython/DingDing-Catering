package com.dingdingcatering.vo;

import com.dingdingcatering.entity.SetMealDish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 套餐视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetMealVO implements Serializable {

    /**
     * 套餐主键
     */
    private Long id;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 套餐名称
     */
    private String name;

    /**
     * 套餐价格
     */
    private BigDecimal price;

    /**
     * 状态：0-停用，1-启用
     */
    private Integer status;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 图片URL
     */
    private String image;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 创建人姓名（冗余字段）
     */
    private String createEmployeeName;

    /**
     * 更新人姓名（冗余字段）
     */
    private String updateEmployeeName;

    /**
     * 套餐与菜品的关联关系列表
     */
    private List<SetMealDish> setmealDishes;
}