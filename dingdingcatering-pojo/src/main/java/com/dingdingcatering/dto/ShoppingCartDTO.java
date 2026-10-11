package com.dingdingcatering.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 购物车数据传输对象
 */
@Data
public class ShoppingCartDTO implements Serializable {

    /** 菜品ID */
    private Long dishId;

    /** 套餐ID */
    private Long setmealId;

    /** 口味信息 */
    private String dishFlavor;

}
