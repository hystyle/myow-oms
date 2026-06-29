package com.myow.user.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user")
public class TenantUserDO {

    @TableId("user_id")
    private Long userId;

    private String tenantId;

    private String userCode;

    private Long deptId;

    private String loginName;

    private Long positionId;

    private String nickName;

    private String userType;

    private String email;

    private String phone;

    private String gender;

    private Long avatar;

    private String password;

    private Boolean status;

    private Boolean adminFlag;

    private Integer failedLoginCount;

    private LocalDateTime lockedUntil;

    private LocalDateTime passwordUpdateTime;

    private LocalDateTime passwordExpireTime;

    private Boolean mustChangePassword;

    private LocalDateTime lastLoginTime;

    private String lastLoginIp;

    private Boolean deletedFlag;

    private Long createDept;

    private Long createBy;

    private LocalDateTime createTime;

    private Long updateBy;

    private LocalDateTime updateTime;

    private String remark;
}
