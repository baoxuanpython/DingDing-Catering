package com.dingdingcatering.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flavor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

        /**
         * 口味类型id（可选，int64）
         */
        private Long id;

        /**
         * 口味类型名称（必填，如"辣度"、"麻度"）
         */
        private String flavorName;
        /**
         * 口味类型值（可选，如"不辣"、"微辣"）
         */
        private String flavorValue;
        /**
         * 口味类型值列表（必填，逗号分隔，如"不辣,微辣,中辣,特辣"）
         */
        private String valueList;
}
