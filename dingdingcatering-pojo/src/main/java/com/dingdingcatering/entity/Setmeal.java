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

/**
 * 套餐实体类（对应数据库表：setmeal）
 */
@Data
@Builder
@AutoFill
@NoArgsConstructor
@AllArgsConstructor
public class Setmeal implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 套餐主键 */
    private Long id;

    /** 分类ID */
    private Long categoryId;

    /** 套餐名称 */
    private String name;

    /** 套餐价格 */
    private BigDecimal price;

    /** 状态：0-停用，1-启用 */
    private Integer status;

    /** 描述信息 */
    private String description;

    /** 图片URL */
    private String image;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 创建人ID */
    private Long createUser;

    /** 更新人ID */
    private Long updateUser;
}
