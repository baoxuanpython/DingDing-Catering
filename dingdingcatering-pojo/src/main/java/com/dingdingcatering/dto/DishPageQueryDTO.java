package com.dingdingcatering.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 菜品分页查询数据传输对象
 */
@Data
public class DishPageQueryDTO implements Serializable {

    /** 页码 */
    private int page;

    /** 每页记录数 */
    private int pageSize;

    /** 菜品名称（模糊查询） */
    private String name;

    /** 分类ID */
    private Long categoryId;

    /** 状态：0-停售，1=起售 */
    private Integer status;

}
