package com.myow.common.ocr.normalize;

import java.util.regex.Pattern;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: ISO 6346 箱号校验。
 * <p>
 * 箱号自带校验位，是 OCR 场景下<b>极其宝贵的自校验信号</b>——
 * 把 0/O、1/I、5/S、8/B 这类高频误识别当场抓出来，避免脏数据流入订单系统。
 */
public final class ContainerNoValidator {

    private static final Pattern PATTERN = Pattern.compile("^[A-Z]{4}\\d{7}$");

    /** ISO 6346 字母权值表：跳过 11 的倍数 */
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private ContainerNoValidator() {
    }

    /** 格式是否符合 4 字母 + 7 数字 */
    public static boolean matchesFormat(String containerNo) {
        return containerNo != null && PATTERN.matcher(containerNo).matches();
    }

    /**
     * 校验位是否正确。
     */
    public static boolean isValid(String containerNo) {
        if (!matchesFormat(containerNo)) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            char c = containerNo.charAt(i);
            int value = Character.isDigit(c) ? (c - '0') : letterValue(c);
            sum += value * (1 << i);
        }
        int expected = sum % 11 % 10;
        int actual = containerNo.charAt(10) - '0';
        return expected == actual;
    }

    /**
     * 字母权值：A=10 起算，依次递增并跳过 11 的倍数（即 11、22、33 不使用）。
     */
    private static int letterValue(char c) {
        int index = LETTERS.indexOf(c);
        if (index < 0) {
            return 0;
        }
        int value = 10;
        for (int i = 0; i < index; i++) {
            value++;
            if (value % 11 == 0) {
                value++;
            }
        }
        return value;
    }
}
