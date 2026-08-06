package com.myow.common.ocr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: DO 签发方（抬头公司）。
 */
@Data
@JsonPropertyOrder({"company_name", "address"})
public class Issuer {

    @JsonProperty("company_name")
    private String companyName;

    /** 多行地址以 ", " 连接后的完整地址 */
    @JsonProperty("address")
    private String address;
}
