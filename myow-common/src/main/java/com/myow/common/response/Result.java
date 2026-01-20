package com.myow.common.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final long OK_CODE = 200;

    public static final String OK_MSG = "操作成功";

    /**
     * 响应结果
     */
    private boolean success;

    /**
     * 业务错误码
     */
    private long code;

    /**
     * 信息描述
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    private Result() {
    }

    private Result(long code, String message, T data) {
        this.success = true;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private Result(boolean success, long code, String message) {
        this.success = true;
        this.code = code;
        this.message = message;
    }

    private Result(ErrorCode errorCode, T data) {
        this.success = false;
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(HttpStatus.OK.value(), "请求成功", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(OK_CODE, OK_MSG, data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(OK_CODE, OK_MSG, data);
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> Result<T> error(ErrorCode errorCode, String message) {
        return new Result<>(errorCode.getCode(), message, null);
    }

    public static <T> Result<T> error(long code, String message) {
        return new Result<>(false, code, message);
    }


}
