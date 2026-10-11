package com.dingdingcatering.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 分类数据传输对象
 */
@Data
public class CategoryDTO implements Serializable {

    /** 分类主键标识 */
    private Long id;

    /** 分类类型：1-菜品分类，2-套餐分类 */
    private Integer type;

    /** 分类名称 */
    private String name;

    /** 排序权重（数值越小越靠前） */
    private Integer sort;

}
