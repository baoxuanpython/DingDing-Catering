package com.dingdingcatering.exception;

/**
 * 不允许删除异常
 * <p>
 * 当删除操作违反业务规则时抛出（如关联数据未清理、状态不允许删除等）
 */
public class DeletionNotAllowedException extends BaseException {

    public DeletionNotAllowedException(String msg) {
        super(msg);
    }

}
