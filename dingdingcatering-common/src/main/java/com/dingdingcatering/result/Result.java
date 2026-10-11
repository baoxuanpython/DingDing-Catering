package com.dingdingcatering.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * <p>
 * 所有 Controller 方法的返回值都使用此封装格式，
 * 包含状态码、消息提示和业务数据，便于前端统一处理响应。
 *
 * @param <T> 业务数据类型
 */
@Data
public class Result<T> implements Serializable {

    /** 状态码：1 表示成功，0 表示失败，其他为自定义错误码 */
    private Integer code;
    /** 错误信息或提示语 */
    private String msg;
    /** 业务数据 */
    private T data;

    /**
     * 返回成功结果（无数据）
     *
     * @return 成功结果对象
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 1;
        return result;
    }

    /**
     * 返回成功结果（带业务数据）
     *
     * @param object 业务数据对象
     * @return 成功结果对象
     */
    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.data = object;
        result.code = 1;
        return result;
    }

    /**
     * 返回失败结果（默认错误码为 0）
     *
     * @param msg 错误信息
     * @return 失败结果对象
     */
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = 0;
        return result;
    }

    /**
     * 返回带自定义错误码的错误结果
     * <p>
     * 用于区分不同类型的错误（如认证失败、业务失败等）
     *
     * @param code 错误码（建议使用 ErrorCode 常量）
     * @param msg  错误信息
     * @return 统一结果对象
     */
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        return result;
    }
}
