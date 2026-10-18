package com.dingdingcatering.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 购物车数据传输对象
 */
@Data
public class ShoppingCartDTO implements Serializable {

    /** 菜品ID */
    private Long dishId;

    /** 套餐ID */
    private Long setmealId;

    /** 口味类型ID列表（前端传数组，后端拼接为逗号分隔字符串） */
    private List<Long> flavorId;

    /** 指定口味值列表（前端传数组，后端拼接为逗号分隔字符串） */
    private List<String> flavorVal;

}