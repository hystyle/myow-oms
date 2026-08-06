package com.myow.common.ocr.model;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 版面数据来源通道。
 */
public enum ParseSource {

    /** 原生 PDF 文本层（PDFBox），零误差、最快 */
    PDF_TEXT_LAYER,

    /** 扫描件 / 图片，走 Tesseract OCR */
    OCR,

    /** 文本层稀疏，自动降级到 OCR */
    OCR_FALLBACK
}
