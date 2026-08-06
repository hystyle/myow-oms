package com.myow.common.ocr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 集装箱信息（当前模板为单箱；多箱场景见 ParseResult#getContainers）。
 */
@Data
@JsonPropertyOrder({"container_no", "size_type", "seal_no", "weight", "quantity"})
public class ContainerInfo {

    /** 箱号，如 MSKU1758984 */
    @JsonProperty("container_no")
    private String containerNo;

    /** 箱型尺寸，如 "40 ft HC Dry" */
    @JsonProperty("size_type")
    private String sizeType;

    /** 封条号 */
    @JsonProperty("seal_no")
    private String sealNo;

    /** 单箱重量 */
    @JsonProperty("weight")
    private String weight;

    /** 单箱件数 */
    @JsonProperty("quantity")
    private String quantity;
}
