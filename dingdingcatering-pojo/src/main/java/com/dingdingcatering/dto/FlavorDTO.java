package com.dingdingcatering.dto;

import com.dingdingcatering.annotation.AutoFill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AutoFill
public class FlavorDTO implements Serializable {
    /**
     * 口味类型id（可选，int64）
     */
    private Long id;
    /**
     * 口味类型名称（必填，全局唯一）
     */
    private String flavorName;
    /**
     * 口味值可选列表（必填，逗号分隔，至少1个值）
     */
    private String valueList;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 修改人
     */
    private Long updateUser;
    /**
     * 创建人姓名（冗余字段）
     */
    private String createEmployeeName;
    /**
     * 修改人姓名（冗余字段）
     */
    private String updateEmployeeName;
}