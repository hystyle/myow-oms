package com.myow.user.system.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLoginLogReqDTO {
    private Long tenantId;
    private Long userId;
    private String loginName;
    private String loginType;
    private String loginClient;
    private String loginIp;
    private String loginLocation;
    private String userAgent;
    private Integer status;
    private String failReason;
    private String traceId;
}
