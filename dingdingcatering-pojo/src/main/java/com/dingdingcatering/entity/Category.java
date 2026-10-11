package com.dingdingcatering.entity;

import com.dingdingcatering.annotation.AutoFill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分类实体类（对应数据库表：category）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AutoFill
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 分类主键 */
    private Long id;

    /** 类型：1-菜品分类，2-套餐分类 */
    private Integer type;

    /** 分类名称 */
    private String name;

    /** 排序权重 */
    private Integer sort;

    /** 状态：0-禁用，1-启用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 创建人ID */
    private Long createUser;

    /** 修改人ID */
    private Long updateUser;
}
