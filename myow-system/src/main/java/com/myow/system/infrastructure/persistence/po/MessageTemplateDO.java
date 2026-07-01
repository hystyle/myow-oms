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
@TableName("sys_message_template")
public class MessageTemplateDO {
    @TableId(value = "template_id", type = IdType.ASSIGN_ID)
    private Long templateId;
    private String templateCode;
    private String channel;
    private String title;
    private String content;
    private String variables;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
