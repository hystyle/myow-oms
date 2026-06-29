package com.myow.user.system.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateConfigReqDTO {
    private Long tenantId;
    private String configKey;
    private String configValue;
    private String configType;
    private String groupCode;
    private Boolean systemFlag;
    private String remark;
}
