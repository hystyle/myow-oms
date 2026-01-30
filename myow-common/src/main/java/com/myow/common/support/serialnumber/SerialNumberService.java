package com.myow.common.support.serialnumber;

import jakarta.annotation.Nullable;

import java.time.LocalDate;

/**
 * @author: yss
 * @date: 2026-01-29 20:46
 * @description: 单号生成器
 */
public interface SerialNumberService {


    String generate(String type, @Nullable LocalDate businessDate);

    String generate(String type, @Nullable LocalDate businessDate, Object businessObj);

}
