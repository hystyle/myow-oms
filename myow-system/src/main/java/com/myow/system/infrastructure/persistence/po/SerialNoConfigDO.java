package com.myow.system.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_serial_no_config")
public class SerialNoConfigDO {

    @TableId("serial_number_id")
    private Integer serialNumberId;

    private String businessName;

    private String format;

    private String ruleType;

    private String remark;

    private Long initNumber;

    private Integer stepRandomRange;

    private Long lastNumber;

    private LocalDateTime lastTime;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
