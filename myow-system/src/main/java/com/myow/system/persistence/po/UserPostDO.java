package com.myow.system.persistence.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户与岗位关联表
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_post")
public class UserPostDO {

    /**
     * 用户ID
     */
    @TableId("user_id")
    private Long userId;

    /**
     * 岗位ID
     */
    private Long postId;
}
