package com.dingdingcatering.dto;

import com.dingdingcatering.entity.Flavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 菜品口味批量更新数据传输对象
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

    /** 口味类型列表 */
    private List<Flavor> flavors;
}
