package com.myow.common.exception;

import com.myow.common.response.ErrorCode;

/**
 * 自定义业务异常
 * 用于处理业务逻辑中出现的预期异常
 */
public class BusinessException extends RuntimeException {
    public BusinessException() {
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
