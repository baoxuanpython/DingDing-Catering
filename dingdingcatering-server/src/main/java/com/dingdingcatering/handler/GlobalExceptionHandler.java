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
 *
 * <p>设计说明：</p>
 * <ul>
 *     <li>统一返回 HTTP 200 状态码，通过 Result#code 区分业务成功/失败</li>
 *     <li>不使用 @ResponseStatus 注解，避免前端 Axios错误处理</li>
 *     <li>详细的异常信息记录在日志中，返回给前端的都是友好的提示信息</li>
 * </ul>
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public Result<String> handleBaseException(BaseException ex) {
        log.error("业务异常：{}", ex.getMessage());
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public Result<String> handleTokenExpiredException(TokenExpiredException ex) {
        log.error("令牌过期：{}", ex.getMessage());
        return Result.error(MessageConstant.JWT_EXPIRED);
    }

    @ExceptionHandler(TokenInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> handleTokenInvalidException(TokenInvalidException ex) {
        log.error("令牌无效：{}", ex.getMessage());
        return Result.error(MessageConstant.JWT_INVALID);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        log.error("SQL约束异常：{}", ex.getMessage());
        String message = ex.getMessage();
        if (message != null && message.contains("Duplicate entry")) {
            return Result.error(MessageConstant.DATA_EXIST);
        }
        throw new BaseException("数据库操作失败：" + message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidationException(MethodArgumentNotValidException ex) {
        String message = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        log.error("参数校验失败：{}", message);
        return Result.error(MessageConstant.PARAM_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception ex) {
        log.error("系统异常", ex);
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

    @ExceptionHandler(SetMealEnableFailedException.class)
    public Result<String> handleSetMealEnableFailedException(SetMealEnableFailedException ex) {
        log.error("套餐启用失败：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(PasswordEditFailedException.class)
    public Result<String> handlePasswordEditFailedException(PasswordEditFailedException ex) {
        log.error("密码修改失败：{}", ex.getMessage());
        return Result.error(MessageConstant.PASSWORD_EDIT_FAILED);
    }

    @ExceptionHandler(UserNotLoginException.class)
    public Result<String> handleUserNotLoginException(UserNotLoginException ex) {
        log.error("用户未登录：{}", ex.getMessage());
        return Result.error(MessageConstant.USER_NOT_LOGIN);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public Result<String> handleAccountNotFoundException(AccountNotFoundException ex) {
        log.error("账号不存在：{}", ex.getMessage());
        return Result.error(MessageConstant.LOGIN_FAILED);
    }

    @ExceptionHandler(UploadFileFailException.class)
    public Result<String> handleUploadFileFailException(UploadFileFailException ex) {
        log.error("上传文件失败：{}", ex.getMessage());
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

    @ExceptionHandler(OrderBusinessException.class)
    public Result<String> handleOrderBusinessException(OrderBusinessException ex) {
        log.error("订单业务异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(DeletionNotAllowedException.class)
    public Result<String> handleDeletionNotAllowedException(DeletionNotAllowedException ex) {
        log.error("删除不允许：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public Result<String> handleSecurityException(SecurityException ex) {
        log.error("安全异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(JsonProcessingException.class)
    public Result<String> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("JSON 序列/反序列化异常：{}", ex.getMessage());
        return Result.error(MessageConstant.JSON_PROCESSING_ERROR);
    }

    @ExceptionHandler(ShoppingCartBusinessException.class)
    public Result<String> handleShoppingCartBusinessException(ShoppingCartBusinessException ex) {
        log.error("购物车业务异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }
}
