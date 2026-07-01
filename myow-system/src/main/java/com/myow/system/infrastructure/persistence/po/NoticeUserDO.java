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
@TableName("sys_notice_user")
public class NoticeUserDO {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    private Long noticeId;
    private Long userId;
    private Integer readStatus;
    private LocalDateTime readTime;
}
