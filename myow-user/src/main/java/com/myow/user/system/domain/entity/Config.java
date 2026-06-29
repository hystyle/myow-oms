package com.myow.user.system.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * System runtime configuration.
 */
@Getter
@Setter
public class Config {
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
