package com.myow.common.exception;

import com.myow.common.response.Result;
import com.myow.common.response.ResultCode;
import com.myow.common.util.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<?> handleValidationException(Exception e) {
        logger.error("URL:{}, 参数校验异常: {}", getCurrentRequestUrl(), e.getMessage(), e);
        String msg = e instanceof MethodArgumentNotValidException ex
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : ((BindException) e).getFieldError().getDefaultMessage();
        return Result.error(ResultCode.PARAM_ERROR, MessageUtils.getMessage(msg));
    }

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e) {
        logger.error("URL:{}, 业务异常: {}", getCurrentRequestUrl(), e.getMessage(), e);
        String message = MessageUtils.getMessage(String.valueOf(e.getErrorCode().getCode()), e.getArgs());
        return Result.error(e.getErrorCode().getCode(), message);
    }

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public Result<String> handleException(Throwable e) {
        logger.error("URL:{}, 系统异常: {}", getCurrentRequestUrl(), e.getMessage(), e);
        return Result.error(ResultCode.SYSTEM_ERROR, MessageUtils.getMessage(String.valueOf(ResultCode.SYSTEM_ERROR.getCode())));
    }

    /**
     * 获取当前请求url
     */
    private String getCurrentRequestUrl() {
        RequestAttributes request = RequestContextHolder.getRequestAttributes();
        if (null == request) {
            return null;
        }
        ServletRequestAttributes servletRequest = (ServletRequestAttributes) request;
        return servletRequest.getRequest().getRequestURI();
    }

}
