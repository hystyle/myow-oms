package com.myow.user.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role")
public class RoleDO {

    @TableId("role_id")
    private Long roleId;

    private String tenantId;

    private String roleName;

    private String roleCode;

    private String status;

    private Boolean deletedFlag;
}
