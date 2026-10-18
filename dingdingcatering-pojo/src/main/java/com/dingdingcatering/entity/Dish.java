package com.dingdingcatering.entity;

import com.dingdingcatering.annotation.AutoFill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜品实体类（对应数据库表：dish）
 */
@Data
@Builder
@AutoFill
@NoArgsConstructor
@AllArgsConstructor
public class Dish implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 创建人ID */
    private Long createUser;

    /** 更新人ID */
    private Long updateUser;

    /** 创建人姓名（冗余字段） */
    private String createEmployeeName;

    /** 更新人姓名（冗余字段） */
    private String updateEmployeeName;

}