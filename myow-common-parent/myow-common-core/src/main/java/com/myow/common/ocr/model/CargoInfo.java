package com.myow.common.ocr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 货物信息。
 */
@Data
@JsonPropertyOrder({"total_packages", "description", "total_weight"})
public class CargoInfo {

    /** 总件数（已剥离千分位） */
    @JsonProperty("total_packages")
    private Integer totalPackages;

    /** 品名描述 */
    @JsonProperty("description")
    private String description;

    /** 总重量，保留单据原文含单位，如 "14,830 LB" */
    @JsonProperty("total_weight")
    private String totalWeight;
}
