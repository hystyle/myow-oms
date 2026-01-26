package com.myow.system.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author gemini
 */
@Getter
@Setter
public class SerialNoRecord {
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
     * 上次生成时间
     */
    private LocalDateTime lastTime;

    /**
     * 当日/当月累计生成数量
     */
    private Long count;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
