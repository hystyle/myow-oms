package com.myow.user.system.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ConfigRespDTO {
    private Long configId;
    private Long tenantId;
    private String configKey;
    private String configValue;
    private String configType;
    private String groupCode;
    private Boolean systemFlag;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
