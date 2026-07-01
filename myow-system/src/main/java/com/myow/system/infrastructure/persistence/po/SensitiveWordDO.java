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
@TableName("sys_sensitive_word")
public class SensitiveWordDO {
    @TableId(value = "word_id", type = IdType.ASSIGN_ID)
    private Long wordId;
    private String word;
    private String category;
    private Integer level;
    private String replacement;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
