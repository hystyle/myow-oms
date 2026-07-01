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
@TableName("sys_site_config")
public class SiteConfigDO {
    @TableId(value = "config_id", type = IdType.ASSIGN_ID)
    private Long configId;
    private String siteCode;
    private String configKey;
    private String configValue;
    private String configType;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
