package com.myow.user.application.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserRespVO {

    private Long userId;

    private String tenantId;

    private Long deptId;

    private String deptName;

    private Long positionId;

    private String positionName;

    private List<Long> roleIdList;

    private List<String> roleNameList;

    private String userName;

    private String nickName;

    private String userType;

    private String email;

    private String phone;

    private String gender;

    private Long avatar;

    private String status;

    private LocalDateTime createTime;

    private String remark;

    private Boolean adminFlag;

    private Integer failedLoginCount;

    private LocalDateTime lockedUntil;

    private LocalDateTime passwordUpdateTime;

    private LocalDateTime passwordExpireTime;

    private Boolean mustChangePassword;

    private LocalDateTime lastLoginTime;

    private String lastLoginIp;
}
