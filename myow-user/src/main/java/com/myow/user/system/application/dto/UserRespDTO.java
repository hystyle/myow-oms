package com.myow.user.system.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yss
 */
@Getter
@Setter
public class UserRespDTO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 岗位id
     */
    private Long positionId;

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 角色id
     */
    private List<Long> roleIdList;

    /**
     * 角色名称
     */
    private List<String> roleNameList;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户类型（sys_user系统用户）
     */
    private String userType;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private String gender;

    /**
     * 头像地址
     */
    private Long avatar;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否为超级管理员
     */
    private Boolean adminFlag;
}
