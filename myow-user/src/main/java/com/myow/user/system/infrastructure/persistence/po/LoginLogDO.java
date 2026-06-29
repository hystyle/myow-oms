package com.myow.user.system.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Login and session security audit log.
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_login_log")
public class LoginLogDO {
    @TableId("login_log_id")
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
