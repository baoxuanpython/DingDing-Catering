package com.dingdingcatering.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 套餐总览
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetMealOverViewVO implements Serializable {
    // 已启售数�?
    private Integer sold;

    // 已停售数�?
    private Integer discontinued;
}
