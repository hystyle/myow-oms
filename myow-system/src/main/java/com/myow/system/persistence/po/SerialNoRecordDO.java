package com.myow.system.persistence.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流水号记录表
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_serial_no_record")
public class SerialNoRecordDO {

    /**
     * 单号规则ID（关联业务规则配置）
     */
    @TableId("serial_number_id")
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
