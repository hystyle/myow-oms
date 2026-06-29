package com.myow.user.system.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LoginLogRespDTO {
    private Long loginLogId;
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
    private LocalDateTime loginTime;
    private String traceId;
}
