package com.dingdingcatering.handler;

import com.dingdingcatering.enumeration.ErrorCode;
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
 * 1. 统一捕获并处理所有 Controller 层抛出的异常
 * 2. 将异常信息转换为标准的 Result 响应格式
 * 3. 设置合理的 HTTP 状态码，便于前端拦截器按状态码分流
 * 4. 记录异常日志便于排查问题
 * <p>
 * 注意：所有携带 {@link ErrorCode} 的异常都通过 {@code ex.getErrorCode()}
 * 动态获取错误码，不再硬编码。这样 Service 层抛异常时传什么 ErrorCode，
 * 前端就收到什么 ErrorCode，保持信息链路完整。
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ==================== 认证模块（HTTP 401） ====================

    /**
     * 处理 JWT 令牌过期异常。
     *
     * @param ex 令牌过期异常
     * @return 错误响应（HTTP 401）
     */
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> handleTokenExpiredException(TokenExpiredException ex) {
        log.error("令牌过期：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理 JWT 令牌无效异常。
     *
     * @param ex 令牌无效异常
     * @return 错误响应（HTTP 401）
     */
    @ExceptionHandler(TokenInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> handleTokenInvalidException(TokenInvalidException ex) {
        log.error("令牌无效：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理用户未登录异常。
     *
     * @param ex 用户未登录异常
     * @return 错误响应（HTTP 401）
     */
    @ExceptionHandler(UserNotLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> handleUserNotLoginException(UserNotLoginException ex) {
        log.error("用户未登录：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理账号不存在异常（登录场景）。
     *
     * @param ex 账号不存在异常
     * @return 错误响应（HTTP 401）
     */
    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> handleAccountNotFoundException(AccountNotFoundException ex) {
        log.error("账号不存在：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理账号被锁定异常。
     *
     * @param ex 账号锁定异常
     * @return 错误响应（HTTP 401）
     */
    @ExceptionHandler(AccountLockedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> handleAccountLockedException(AccountLockedException ex) {
        log.error("账号被锁定：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理密码错误异常。
     *
     * @param ex 密码错误异常
     * @return 错误响应（HTTP 401）
     */
    @ExceptionHandler(PasswordErrorException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> handlePasswordErrorException(PasswordErrorException ex) {
        log.error("密码错误：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理登录失败异常。
     *
     * @param ex 登录失败异常
     * @return 错误响应（HTTP 401）
     */
    @ExceptionHandler(LoginFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> handleLoginFailedException(LoginFailedException ex) {
        log.error("登录失败：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    // ==================== 参数校验（HTTP 400） ====================

    /**
     * 处理参数校验失败异常（@Valid/@Validated）。
     *
     * @param ex 参数校验异常
     * @return 错误响应（HTTP 400）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleValidationException(MethodArgumentNotValidException ex) {
        String message = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        log.error("参数校验失败：{}", message);
        return Result.error(ErrorCode.PARAM_ERROR, message);
    }

    // ==================== 权限不足（HTTP 403） ====================

    /**
     * 处理安全异常（无权操作）。
     *
     * @param ex 安全异常
     * @return 错误响应（HTTP 403）
     */
    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<String> handleSecurityException(SecurityException ex) {
        log.error("安全异常：{}", ex.getMessage());
        return Result.error(ErrorCode.SECURITY_ERROR, ex.getMessage());
    }

    // ==================== 资源冲突（HTTP 409） ====================

    /**
     * 处理 SQL 唯一约束冲突异常。
     * <p>
     * 典型场景：新增员工时账号已存在。
     *
     * @param ex SQL 约束异常
     * @return 错误响应（HTTP 409）
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Result<String> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        log.error("SQL 约束异常：{}", ex.getMessage());
        String message = ex.getMessage();
        if (message != null && message.contains("Duplicate entry")) {
            return Result.error(ErrorCode.DATA_EXIST, "");
        }
        throw new BaseException(ErrorCode.UNKNOWN_ERROR, "数据库操作失败：" + message);
    }

    /**
     * 处理不允许删除异常。
     * <p>
     * 典型场景：删除起售菜品/套餐、删除有关联数据的分类。
     *
     * @param ex 删除不允许异常
     * @return 错误响应（HTTP 409）
     */
    @ExceptionHandler(DeletionNotAllowedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Result<String> handleDeletionNotAllowedException(DeletionNotAllowedException ex) {
        log.error("删除不允许：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理套餐启用失败异常。
     *
     * @param ex 套餐启用失败异常
     * @return 错误响应（HTTP 409）
     */
    @ExceptionHandler(SetMealEnableFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Result<String> handleSetMealEnableFailedException(SetMealEnableFailedException ex) {
        log.error("套餐启用失败：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理套餐无菜品异常。
     *
     * @param ex 套餐无菜品异常
     * @return 错误响应（HTTP 409）
     */
    @ExceptionHandler(SetMealNoDishesException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Result<String> handleSetMealNoDishesException(SetMealNoDishesException ex) {
        log.error("套餐无菜品：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    // ==================== 业务规则不满足（HTTP 422） ====================

    /**
     * 处理密码修改失败异常。
     *
     * @param ex 密码修改失败异常
     * @return 错误响应（HTTP 422）
     */
    @ExceptionHandler(PasswordEditFailedException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Result<String> handlePasswordEditFailedException(PasswordEditFailedException ex) {
        log.error("密码修改失败：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理订单业务异常。
     *
     * @param ex 订单业务异常
     * @return 错误响应（HTTP 422）
     */
    @ExceptionHandler(OrderBusinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Result<String> handleOrderBusinessException(OrderBusinessException ex) {
        log.error("订单业务异常：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理购物车业务异常。
     *
     * @param ex 购物车业务异常
     * @return 错误响应（HTTP 422）
     */
    @ExceptionHandler(ShoppingCartBusinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Result<String> handleShoppingCartBusinessException(ShoppingCartBusinessException ex) {
        log.error("购物车业务异常：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理口味类型业务异常。
     *
     * @param ex 口味类型业务异常
     * @return 错误响应（HTTP 422）
     */
    @ExceptionHandler(FlavorException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Result<String> handleFlavorException(FlavorException ex) {
        log.error("口味类型业务异常：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理地址簿业务异常。
     *
     * @param ex 地址簿业务异常
     * @return 错误响应（HTTP 422）
     */
    @ExceptionHandler(AddressBookBusinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Result<String> handleAddressBookBusinessException(AddressBookBusinessException ex) {
        log.error("地址业务异常：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    // ==================== 服务器错误（HTTP 500） ====================

    /**
     * 处理文件上传失败异常。
     *
     * @param ex 文件上传失败异常
     * @return 错误响应（HTTP 500）
     */
    @ExceptionHandler(UploadFileFailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleUploadFileFailException(UploadFileFailException ex) {
        log.error("上传文件失败：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理 JSON 序列化/反序列化异常。
     *
     * @param ex JSON 处理异常
     * @return 错误响应（HTTP 500）
     */
    @ExceptionHandler(JsonProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("JSON 序列/反序列化异常：{}", ex.getMessage());
        return Result.error(ErrorCode.JSON_PROCESSING_ERROR, "");
    }

    /**
     * 处理基础业务异常（兜底业务异常）。
     * <p>
     * 注意：如果某个 BaseException 子类没有被对应的 @ExceptionHandler 捕获，
     * 会走到这里。日志会打印具体异常类型，便于发现遗漏的处理器。
     *
     * @param ex 业务异常
     * @return 错误响应（HTTP 500）
     */
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleBaseException(BaseException ex) {
        log.error("未注册的业务异常被兜底：[{}] {}", ex.getClass().getSimpleName(), ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理未捕获的系统级异常（最终兜底）。
     *
     * @param ex 系统异常
     * @return 错误响应（HTTP 500）
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleException(Exception ex) {
        log.error("系统异常", ex);
        return Result.error(ErrorCode.UNKNOWN_ERROR, "");
    }
}
