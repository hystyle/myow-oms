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
@TableName("sys_job_log")
public class JobLogDO {
    @TableId(value = "log_id", type = IdType.ASSIGN_ID)
    private Long logId;
    private Long jobId;
    private String jobName;
    private String jobGroup;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long costTime;
    private String status;
    private String errorMsg;
}
