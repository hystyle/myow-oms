package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author yss
 */
@Getter
@Setter
public class UpdateSerialNoRecordReqDTO {
    /**
     * 单号规则ID（关联业务规则配置）
     */
    private Integer serialNumberId;

    /**
     * 记录日期（按天/月/年记录）
     */
    private LocalDate recordDate;

    /**
     * 最后生成的流水号
     */
    private Long lastNumber;

    /**
     * 当日/当月累计生成数量
     */
    private Long count;
}
