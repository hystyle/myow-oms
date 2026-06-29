package com.myow.user.system.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Login and session security audit log.
 */
@Getter
@Setter
public class LoginLog {
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
