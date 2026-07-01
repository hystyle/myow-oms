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
@TableName("sys_export_task")
public class ExportTaskDO {
    @TableId(value = "task_id", type = IdType.ASSIGN_ID)
    private Long taskId;
    private String moduleName;
    private String exportType;
    private String queryParams;
    private String status;
    private Long fileId;
    private Long fileSize;
    private String errorMsg;
    private Long createBy;
    private LocalDateTime createTime;
    private LocalDateTime finishTime;
}
