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

    SQL_INJECTION_ERROR(10004, "存在SQL注入风险，请联系技术工作人员！"),

    SERIAL_NUMBER_NOT_FOUND(10005, "没有找到支持类型%s的单号生成器"),
    SERIAL_NUMBER_LOCK_TIMEOUT(10006, "获取单号锁超时,type=%s,date=%s"),
    SERIAL_NUMBER_GENERATE_FAILED(10007, "生成单号失败,第%s次尝试"),
    ;

    private final long code;
    private final String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }
}
