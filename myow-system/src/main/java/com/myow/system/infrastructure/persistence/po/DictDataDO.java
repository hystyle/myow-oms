package com.myow.system.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yss
 * @since 2026-01-26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_dict_data")
public class DictDataDO {

    @TableId(value = "dict_data_id", type = IdType.AUTO)
    private Long dictDataId;

    private Long dictId;

    private String dataValue;

    private String dataLabel;

    private String remark;

    private Integer sort;

    private Boolean disabledFlag;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
