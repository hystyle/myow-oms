package com.myow.common.ocr;

import com.myow.common.ocr.config.OcrConfig;
import com.myow.common.ocr.model.ParseResult;

import java.nio.file.Paths;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: DO 解析命令行演示：java ... DeliveryOrderParserDemo /path/to/do.pdf
 */
public class DeliveryOrderParserDemo {

    private static final String DEFAULT_PDF = "/Volumes/yss/agentic/file/EZI-0022777-9-DO-1.pdf";

    public static void main(String[] args) {
        OcrConfig config = OcrConfig.defaults();

        DeliveryOrderParser parser = new DeliveryOrderParser(config);
        ParseResult result = parser.parse(Paths.get(DEFAULT_PDF));

        System.out.println(parser.toJson(result));
        System.out.println();
        System.out.println("---- 解析元信息 ----");
        System.out.println("来源通道 : " + result.getSource());
        System.out.println("页数     : " + result.getPageCount());
        System.out.println("耗时(ms) : " + result.getCostMillis());
        System.out.println("置信度   : " + result.getAvgConfidence());
        System.out.println("箱数     : " + result.getContainers().size());
        System.out.println("缺失字段 : " + result.getMissingFields());
        System.out.println("告警     : " + result.getWarnings());
        System.out.println("需人工复核: " + result.needsManualReview());
    }
}
