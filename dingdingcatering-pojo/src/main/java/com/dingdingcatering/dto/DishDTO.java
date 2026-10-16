package com.dingdingcatering.dto;

import com.dingdingcatering.entity.Flavor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 菜品数据传输对象
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DishDTO implements Serializable {

    /** 菜品主键 */
    private Long id;

    /** 菜品名称 */
    private String name;

    /** 菜品分类ID */
    private Long categoryId;

    /** 菜品价格 */
    private BigDecimal price;

    /** 图片URL */
    private String image;

    /** 描述信息 */
    private String description;

    /** 状态：0-停售，1-起售 */
    private Integer status;

    /** 口味列表 */
    private List<Flavor> flavors;

}
