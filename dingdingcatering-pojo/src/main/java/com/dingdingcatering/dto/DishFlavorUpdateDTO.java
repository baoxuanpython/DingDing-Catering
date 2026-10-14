package com.dingdingcatering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜品口味更新数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishFlavorUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 菜品ID */
    private Long dishId;

    /** 口味类型ID列表（逗号分隔字符串） */
    private String flavors;
}
