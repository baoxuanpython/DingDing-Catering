package com.dingdingcatering.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 套餐内单个菜品的口味选择
 * <p>
 * 一个菜品可能有多组口味类型，flavorId 与 flavorVal 按下标一一对应
 */
@Data
public class SetmealFlavorDTO implements Serializable {

    /** 套餐内菜品ID */
    private Long dishId;

    /** 该菜品的口味类型ID列表 */
    private List<Long> flavorId;

    /** 该菜品选中的口味值列表，与 flavorId 一一对应 */
    private List<String> flavorVal;

}