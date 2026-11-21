package com.dingdingcatering.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 地址簿数据传输对象
 */
@Data
public class AddressBookDTO implements Serializable {

    /** 地址ID（修改时必填） */
    private Long id;

    /** 详细地址（必填） */
    private String detail;

    /** 手机号（必填） */
    private String phone;

    /** 性别（必填） */
    private String sex;

    /** 收件人姓名 */
    private String consignee;

    /** 省份编码 */
    private String provinceCode;

    /** 省份名称 */
    private String provinceName;

    /** 城市编码 */
    private String cityCode;

    /** 城市名称 */
    private String cityName;

    /** 区县编码 */
    private String districtCode;

    /** 区县名称 */
    private String districtName;

    /** 标签（如：家、公司） */
    private String label;

    /** 是否默认地址：1=是，0=否 */
    private Integer isDefault;
}