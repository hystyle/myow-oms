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
@TableName("sys_notice")
public class NoticeDO {
    @TableId(value = "notice_id", type = IdType.ASSIGN_ID)
    private Long noticeId;
    private String title;
    private String content;
    private String noticeType;
    private Integer status;
    private LocalDateTime publishTime;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean deletedFlag;
}
