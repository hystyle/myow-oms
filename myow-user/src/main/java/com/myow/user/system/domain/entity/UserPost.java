package com.myow.user.system.domain.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class UserPost {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 岗位ID
     */
    private Long positionId;
}
