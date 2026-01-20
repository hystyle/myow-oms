package com.myow.common.response;


import lombok.Getter;

/**
 * @author: yss
 * @date: 2026-01-20 20:50
 * @description: 系统错误码
 */
@Getter
public enum ResultCode implements ErrorCode {

    PARAM_ERROR(10001, "参数错误"),

    BUSINESS_HANDING(10002, "业务繁忙，请稍后重试~"),

    SYSTEM_ERROR(10003, "系统似乎出现了点小问题"),
    ;

    private final long code;
    private final String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }
}
