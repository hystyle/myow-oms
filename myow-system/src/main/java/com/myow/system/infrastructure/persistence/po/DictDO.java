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
@TableName("sys_dict")
public class DictDO {

    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long dictId;

    private String dictName;

    private String dictCode;

    private String remark;

    private Long createBy;

    private LocalDateTime createTime;

    private Long updateBy;

    private LocalDateTime updateTime;
}
