package com.myow.common.ocr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 运输信息。
 */
@Data
@JsonPropertyOrder({"carrier", "bl_or_awb_no", "house_no", "location",
        "origin_destination_port", "arrival_date", "free_time_exp"})
public class ShipmentDetails {

    /** 承运人 + 船名航次，如 MAEU MAERSK LUZ 625E */
    @JsonProperty("carrier")
    private String carrier;

    /** 主提单 / 空运单号 */
    @JsonProperty("bl_or_awb_no")
    private String blOrAwbNo;

    /** 分单号 HOUSE NO. */
    @JsonProperty("house_no")
    private String houseNo;

    /** 提货堆场 / 码头 */
    @JsonProperty("location")
    private String location;

    /** 起运地 / 目的港 */
    @JsonProperty("origin_destination_port")
    private String originDestinationPort;

    /** 到港日期 */
    @JsonProperty("arrival_date")
    private String arrivalDate;

    /** 免箱期截止日 */
    @JsonProperty("free_time_exp")
    private String freeTimeExp;
}
