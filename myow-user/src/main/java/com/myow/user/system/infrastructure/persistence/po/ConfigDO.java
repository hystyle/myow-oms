package com.myow.user.system.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * System runtime configuration.
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_config")
public class ConfigDO {
    @TableId(value = "config_id", type = IdType.ASSIGN_ID)
    private Long configId;
    private Long tenantId;
    private String configKey;
    private String configValue;
    private String configType;
    private String groupCode;
    private Boolean systemFlag;
    private String remark;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Boolean deletedFlag;
}
