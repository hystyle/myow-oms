package com.myow.common.ocr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: DO 单据自身信息。
 */
@Data
@JsonPropertyOrder({"do_number", "our_ref_no", "date", "cust_ref_no"})
public class DoInfo {

    /** DO 号，取自单据 ENTRY-B/L NO. 栏，如 EZI-0022777-9 */
    @JsonProperty("do_number")
    private String doNumber;

    /** 内部参考号 OUR REF. NO. */
    @JsonProperty("our_ref_no")
    private String ourRefNo;

    /** 签发日期（保留单据原始书写格式） */
    @JsonProperty("date")
    private String date;

    /** 客户参考号 CUST. REF. NO. */
    @JsonProperty("cust_ref_no")
    private String custRefNo;
}
