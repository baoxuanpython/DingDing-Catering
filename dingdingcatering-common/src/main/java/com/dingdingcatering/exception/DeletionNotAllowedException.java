package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 不允许删除异常
 * <p>
 * 当删除操作违反业务规则时抛出（如关联数据未清理、状态不允许删除等）
 */
public class DeletionNotAllowedException extends BaseException {

    public DeletionNotAllowedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DeletionNotAllowedException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
