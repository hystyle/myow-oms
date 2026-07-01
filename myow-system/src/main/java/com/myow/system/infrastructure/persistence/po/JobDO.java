package com.myow.system.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_job")
public class JobDO {
    @TableId(value = "job_id", type = IdType.ASSIGN_ID)
    private Long jobId;
    private String jobName;
    private String jobGroup;
    private String cronExpression;
    private String handlerName;
    private String executePolicy;
    private Boolean concurrent;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean deletedFlag;
}
