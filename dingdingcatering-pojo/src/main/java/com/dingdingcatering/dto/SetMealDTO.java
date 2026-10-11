package com.dingdingcatering.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.dingdingcatering.entity.SetMealDish;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 套餐数据传输对象
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SetMealDTO implements Serializable {

    /** 套餐主键 */
    private Long id;

    /** 分类ID */
    private Long categoryId;

    /** 套餐名称 */
    private String name;

    /** 套餐价格 */
    private BigDecimal price;

    /** 状态：0-停用，1-启用 */
    private Integer status;

    /** 描述信息 */
    private String description;

    /** 图片URL */
    private String image;

    /** 套餐菜品关联列表 */
    private List<SetMealDish> setmealDishes = new ArrayList<>();

}
