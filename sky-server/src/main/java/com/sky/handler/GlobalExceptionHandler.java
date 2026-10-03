package com.sky.handler;

import com.sky.exception.AccountNotFoundException;
import com.sky.exception.BaseException;
import com.sky.exception.UploadFileFailException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler
    public Result alreadyExistExceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error("SQL完整性约束异常：{}", ex.getMessage());
        String message = ex.getMessage();
        if (message != null && message.contains("Duplicate entry")) {
            return Result.error("数据已存在");
        }
        throw new BaseException("数据库操作失败：" + message);
    }

    /**
     * 处理参数校验异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        log.error("参数校验失败：{}", message);
        return Result.error("参数校验失败");
    }

    @ExceptionHandler
    public Result handleAccountNotFoundException(AccountNotFoundException ex) {
        log.error(ex.getMessage());
        return Result.error("账号不存在");
    }

    @ExceptionHandler
    public Result handleUploadFileFailException(UploadFileFailException ex) {
        log.error("上传文件异常信息：{}", ex.getMessage());
        return Result.error("上传文件失败");
    }
}
