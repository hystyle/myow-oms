package com.myow.common.exception;

import com.myow.common.response.ResultCode;
import lombok.Getter;

/**
 * @author: yss
 * @date: 2026-01-29 21:05
 * @description: 自定义序列号异常
 */
@Getter
public class SerialNumberException extends RuntimeException {

    private final long code;
    private final Object[] args;

    /**
     * 通过错误码枚举和消息参数创建异常
     */
    public SerialNumberException(ResultCode resultCode, Object... args) {
        super(formatMessage(resultCode.getMessage(), args));
        this.code = resultCode.getCode();
        this.args = args != null ? args : new Object[0];
    }

    /**
     * 通过 code 和 message 直接创建异常
     */
    public SerialNumberException(long code, String message, Object... args) {
        super(formatMessage(message, args));
        this.code = code;
        this.args = args != null ? args : new Object[0];
    }

    /**
     * 格式化消息（支持占位符替换）
     */
    private static String formatMessage(String message, Object[] args) {
        if (args == null || args.length == 0 || message == null) {
            return message;
        }
        return String.format(message, args);
    }

    /**
     * 获取错误码枚举（如果有对应的枚举）
     */
    public ResultCode getResultCode() {
        for (ResultCode rc : ResultCode.values()) {
            if (rc.getCode() == this.code) {
                return rc;
            }
        }
        return null;
    }
}
