package com.dingdingcatering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜品销量数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishSalesDTO implements Serializable {

    /** 菜品名称 */
    private String name;

    /** 销量 */
    private Long sales;

}
