package com.myow.user.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_role")
public class UserRoleDO {

    private Long userId;

    private Long roleId;

    public UserRoleDO() {
    }

    public UserRoleDO(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
