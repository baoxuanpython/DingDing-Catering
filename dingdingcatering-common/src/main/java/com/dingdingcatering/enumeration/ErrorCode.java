package com.dingdingcatering.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务错误码枚举
 * <p>
 * 按业务模块分组编号，与 HTTP 状态码配合使用：
 * - HTTP 状态码：标识错误大类（认证/参数/权限/冲突/服务器）
 * - 业务错误码：标识具体错误原因，便于前端精准提示和后端统计
 *
 * <p>号段分配：
 * <ul>
 *   <li>1xxx - 认证模块</li>
 *   <li>2xxx - 员工模块</li>
 *   <li>3xxx - 分类模块</li>
 *   <li>4xxx - 菜品模块</li>
 *   <li>5xxx - 套餐模块</li>
 *   <li>6xxx - 订单模块</li>
 *   <li>7xxx - 购物车模块</li>
 *   <li>8xxx - 地址簿模块</li>
 *   <li>9xxx - 通用/系统模块</li>
 * </ul>
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // ========== 认证模块 1xxx ==========
    TOKEN_EXPIRED(1001, "令牌已过期"),
    TOKEN_INVALID(1002, "令牌无效"),
    USER_NOT_LOGIN(1003, "用户未登录"),
    USER_NOT_FOUND(1004, "用户不存在或已被删除"),
    ACCOUNT_NOT_FOUND(1005, "账号或密码错误"),
    ACCOUNT_DISABLED(1006, "账号已被禁用"),
    PASSWORD_ERROR(1007, "账号或密码错误"),
    LOGIN_FAILED(1008, "登录失败"),

    // ========== 员工模块 2xxx ==========
    PASSWORD_EDIT_FAILED(2001, "密码修改失败"),
    SECURITY_ERROR(2002, "无权为其他用户操作数据"),

    // ========== 分类模块 3xxx ==========
    CATEGORY_RELATED_BY_SETMEAL(3001, "当前分类关联了套餐，不能删除"),
    CATEGORY_RELATED_BY_DISH(3002, "当前分类关联了菜品，不能删除"),

    // ========== 菜品模块 4xxx ==========
    DISH_ON_SALE(4001, "起售中的菜品不能删除"),
    DISH_RELATED_BY_SETMEAL(4002, "当前菜品关联了套餐，不能删除"),
    DISH_IS_NULL(4003, "菜品不存在"),

    // ========== 套餐模块 5xxx ==========
    SETMEAL_ON_SALE(5001, "起售中的套餐不能删除"),
    SETMEAL_ENABLE_FAILED(5002, "套餐内包含未启售菜品，无法启用"),
    SETMEAL_IS_NULL(5003, "套餐不存在"),
    SETMEAL_NO_DISHES(5004, "套餐未关联任何菜品"),

    // ========== 订单模块 6xxx ==========
    ORDER_STATUS_ERROR(6001, "订单状态错误"),
    ORDER_NOT_FOUND(6002, "订单不存在"),
    ORDER_BUSINESS_ERROR(6003, "订单业务异常"),

    // ========== 购物车模块 7xxx ==========
    SHOPPING_CART_IS_NULL(7001, "购物车数据为空，不能下单"),
    SHOPPING_CART_ERROR(7002, "购物车业务异常"),

    // ========== 地址簿模块 8xxx ==========
    ADDRESS_BOOK_IS_NULL(8001, "用户地址为空，不能下单"),
    ADDRESS_BOOK_ERROR(8002, "地址业务异常"),
    // ========== 口味类型模块 9xxx ==========
    FLAVOR_NAME_EXIST(9001, "口味类型名称已存在"),
    FLAVOR_DELETE_FAILED(9006, "口味类型关联了菜品，不能删除"),
    // ========== 通用/系统模块 99xxx ==========
    PARAM_ERROR(99001, "参数校验失败"),
    DATA_EXIST(99002, "数据已存在"),
    UPLOAD_FAILED(99003, "文件上传失败"),
    JSON_PROCESSING_ERROR(99004, "JSON 序列化/反序列化异常"),
    DELETION_NOT_ALLOWED(99005, "不允许删除"),
    UNKNOWN_ERROR(99999, "未知错误");


    /**
     * 业务错误码
     */
    private final Integer code;
    /**
     * 错误描述
     */
    private final String message;
}
