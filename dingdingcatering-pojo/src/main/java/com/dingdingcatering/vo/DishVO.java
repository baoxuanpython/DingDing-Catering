package com.dingdingcatering.vo;

import com.dingdingcatering.entity.Flavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜品视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishVO implements Serializable {

    /**
     * 菜品主键
     */
    private Long id;

    /**
     * 菜品名称
     */
    private String name;

    /**
     * 菜品分类ID
     */
    private Long categoryId;

    /**
     * 菜品价格
     */
    private BigDecimal price;

    /**
     * 图片URL
     */
    private String image;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 状态：0-停售，1-起售
     */
    private Integer status;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 创建人姓名（冗余字段）
     */
    private String createEmployeeName;

    /**
     * 更新人姓名（冗余字段）
     */
    private String updateEmployeeName;

    /**
     * 菜品关联的口味类型列表
     */
    private List<Flavor> flavors;

}