package com.myow.common.ocr;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 单据解析异常。
 */
public class DocumentParseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DocumentParseException(String message) {
        super(message);
    }

    public DocumentParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
