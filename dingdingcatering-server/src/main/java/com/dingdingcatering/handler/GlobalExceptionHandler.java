package com.dingdingcatering.handler;

import com.dingdingcatering.constant.MessageConstant;
import com.dingdingcatering.exception.*;
import com.dingdingcatering.result.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;

/**
 * 全局异常处理器
 * <p>
 * 职责：
 * 1. 统一捕获并处理所有Controller层抛出的异常
 * 2. 将异常信息转换为标准的Result响应格式
 * 3. 记录异常日志便于排查问题
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理基础业务异常。
     *
     * @param ex 业务异常
     * @return 错误响应
     */
    @ExceptionHandler(BaseException.class)
    public Result<String> handleBaseException(BaseException ex) {
        log.error("业务异常：{}", ex.getMessage());
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

    /**
     * 处理JWT令牌过期异常。
     *
     * @param ex 令牌过期异常
     * @return 错误响应
     */
    @ExceptionHandler(TokenExpiredException.class)
    public Result<String> handleTokenExpiredException(TokenExpiredException ex) {
        log.error("令牌过期：{}", ex.getMessage());
        return Result.error(MessageConstant.JWT_EXPIRED);
    }

    /**
     * 处理JWT令牌无效异常。
     *
     * @param ex 令牌无效异常
     * @return 错误响应（HTTP 401）
     */
    @ExceptionHandler(TokenInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> handleTokenInvalidException(TokenInvalidException ex) {
        log.error("令牌无效：{}", ex.getMessage());
        return Result.error(MessageConstant.JWT_INVALID);
    }

    /**
     * 处理SQL唯一约束冲突异常。
     * <p>
     * 典型场景：新增员工时账号已存在。
     *
     * @param ex SQL约束异常
     * @return 错误响应
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        log.error("SQL约束异常：{}", ex.getMessage());
        String message = ex.getMessage();
        if (message != null && message.contains("Duplicate entry")) {
            return Result.error(MessageConstant.DATA_EXIST);
        }
        throw new BaseException("数据库操作失败：" + message);
    }

    /**
     * 处理参数校验失败异常（@Valid/@Validated）。
     *
     * @param ex 参数校验异常
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidationException(MethodArgumentNotValidException ex) {
        String message = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        log.error("参数校验失败：{}", message);
        return Result.error(MessageConstant.PARAM_ERROR);
    }

    /**
     * 处理未捕获的系统级异常（兜底处理）。
     *
     * @param ex 系统异常
     * @return 错误响应
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception ex) {
        log.error("系统异常", ex);
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

    /**
     * 处理套餐启用失败异常。
     *
     * @param ex 套餐启用失败异常
     * @return 错误响应
     */
    @ExceptionHandler(SetMealEnableFailedException.class)
    public Result<String> handleSetMealEnableFailedException(SetMealEnableFailedException ex) {
        log.error("套餐启用失败：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理密码修改失败异常。
     *
     * @param ex 密码修改失败异常
     * @return 错误响应
     */
    @ExceptionHandler(PasswordEditFailedException.class)
    public Result<String> handlePasswordEditFailedException(PasswordEditFailedException ex) {
        log.error("密码修改失败：{}", ex.getMessage());
        return Result.error(MessageConstant.PASSWORD_EDIT_FAILED);
    }

    /**
     * 处理用户未登录异常。
     *
     * @param ex 用户未登录异常
     * @return 错误响应
     */
    @ExceptionHandler(UserNotLoginException.class)
    public Result<String> handleUserNotLoginException(UserNotLoginException ex) {
        log.error("用户未登录：{}", ex.getMessage());
        return Result.error(MessageConstant.USER_NOT_LOGIN);
    }

    /**
     * 处理账号不存在异常。
     *
     * @param ex 账号不存在异常
     * @return 错误响应
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public Result<String> handleAccountNotFoundException(AccountNotFoundException ex) {
        log.error("账号不存在：{}", ex.getMessage());
        return Result.error(MessageConstant.LOGIN_FAILED);
    }

    /**
     * 处理文件上传失败异常。
     *
     * @param ex 文件上传失败异常
     * @return 错误响应
     */
    @ExceptionHandler(UploadFileFailException.class)
    public Result<String> handleUploadFileFailException(UploadFileFailException ex) {
        log.error("上传文件失败：{}", ex.getMessage());
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

    /**
     * 处理订单业务异常。
     *
     * @param ex 订单业务异常
     * @return 错误响应
     */
    @ExceptionHandler(OrderBusinessException.class)
    public Result<String> handleOrderBusinessException(OrderBusinessException ex) {
        log.error("订单业务异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理不允许删除异常。
     * <p>
     * 典型场景：删除起售菜品/套餐、删除有关联数据的分类。
     *
     * @param ex 删除不允许异常
     * @return 错误响应
     */
    @ExceptionHandler(DeletionNotAllowedException.class)
    public Result<String> handleDeletionNotAllowedException(DeletionNotAllowedException ex) {
        log.error("删除不允许：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理安全异常。
     *
     * @param ex 安全异常
     * @return 错误响应
     */
    @ExceptionHandler(SecurityException.class)
    public Result<String> handleSecurityException(SecurityException ex) {
        log.error("安全异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理JSON序列化/反序列化异常。
     *
     * @param ex JSON处理异常
     * @return 错误响应
     */
    @ExceptionHandler(JsonProcessingException.class)
    public Result<String> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("JSON 序列/反序列化异常：{}", ex.getMessage());
        return Result.error(MessageConstant.JSON_PROCESSING_ERROR);
    }

    /**
     * 处理购物车业务异常。
     *
     * @param ex 购物车业务异常
     * @return 错误响应
     */
    @ExceptionHandler(ShoppingCartBusinessException.class)
    public Result<String> handleShoppingCartBusinessException(ShoppingCartBusinessException ex) {
        log.error("购物车业务异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理地址簿业务异常。
     *
     * @param ex 地址簿业务异常
     * @return 错误响应
     */
    @ExceptionHandler(AddressBookBusinessException.class)
    public Result<String> handleAddressBookBusinessException(AddressBookBusinessException ex) {
        log.error("地址业务异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }
}
