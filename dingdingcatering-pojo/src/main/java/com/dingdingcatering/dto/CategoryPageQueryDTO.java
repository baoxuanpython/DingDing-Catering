package com.dingdingcatering.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 分类分页查询数据传输对象
 */
@Data
public class CategoryPageQueryDTO implements Serializable {

    /** 页码 */
    private int page;

    /** 每页记录数 */
    private int pageSize;

    /** 分类名称（模糊查询） */
    private String name;

    /** 分类类型：1-菜品分类，2-套餐分类 */
    private Integer type;

}
