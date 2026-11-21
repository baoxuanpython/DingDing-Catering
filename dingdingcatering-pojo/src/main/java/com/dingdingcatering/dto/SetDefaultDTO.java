package com.dingdingcatering.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 设置默认地址数据传输对象
 */
@Data
public class SetDefaultDTO implements Serializable {

    /** 地址ID（必填） */
    private Long id;
}