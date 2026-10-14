package com.dingdingcatering.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishFlavorVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 口味主键 */
    private Long id;

    /** 菜品ID */
    private Long dishId;

    /** 口味名称 */
    private String name;

    /** 口味数据值 */
    private String flavorVal;

}
