package com.myow.common.ocr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: Delivery Order 标准单证模型（Canonical Schema）。
 * <p>
 * 该结构与对外输出 JSON 严格一一对应，<b>不掺杂任何解析元信息</b>，
 * 便于直接投喂下游订单创建接口。解析过程的来源/耗时/告警等放在 {@link ParseResult}。
 */
@Data
@JsonPropertyOrder({"do_info", "issuer", "shipment_details", "cargo", "container"})
public class DeliveryOrderDoc {

    @JsonProperty("do_info")
    private DoInfo doInfo = new DoInfo();

    @JsonProperty("issuer")
    private Issuer issuer = new Issuer();

    @JsonProperty("shipment_details")
    private ShipmentDetails shipmentDetails = new ShipmentDetails();

    @JsonProperty("cargo")
    private CargoInfo cargo = new CargoInfo();

    @JsonProperty("container")
    private ContainerInfo container = new ContainerInfo();
}
