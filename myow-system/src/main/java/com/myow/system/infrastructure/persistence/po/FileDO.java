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
@TableName("sys_file")
public class FileDO {
    @TableId(value = "file_id", type = IdType.ASSIGN_ID)
    private Long fileId;
    private String moduleName;
    private String originalName;
    private String storageType;
    private String storageKey;
    private Long fileSize;
    private String contentType;
    private LocalDateTime createTime;
    private Boolean deletedFlag;
}
