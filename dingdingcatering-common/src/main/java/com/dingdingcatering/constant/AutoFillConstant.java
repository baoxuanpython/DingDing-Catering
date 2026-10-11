package com.dingdingcatering.constant;

/**
 * 公共字段自动填充相关常量
 * <p>
 * 定义实体类中用于设置审计字段的方法名，配合 @AutoFill 注解使用。
 */
public class AutoFillConstant {
    /** 设置创建时间的方法名 */
    public static final String SET_CREATE_TIME = "setCreateTime";
    /** 设置更新时间的方法名 */
    public static final String SET_UPDATE_TIME = "setUpdateTime";
    /** 设置创建人的方法名 */
    public static final String SET_CREATE_USER = "setCreateUser";
    /** 设置更新人的方法名 */
    public static final String SET_UPDATE_USER = "setUpdateUser";
}
