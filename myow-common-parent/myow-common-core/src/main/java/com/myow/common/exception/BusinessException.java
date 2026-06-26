package com.myow.common.exception;

import com.myow.common.response.ErrorCode;
import lombok.Getter;

/**
 * 自定义业务异常
 * 用于处理业务逻辑中出现的预期异常
 */
@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final transient Object[] args;

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.args = args;
    }
}
