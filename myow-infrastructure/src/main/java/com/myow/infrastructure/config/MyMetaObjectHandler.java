package com.myow.infrastructure.config;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充处理器
 *
 * @author Gemini
 * @since 2026-01-19
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 自动填充创建时间
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime::now, LocalDateTime.class);
        // 自动填充创建人
        this.strictInsertFill(metaObject, "createBy", this::getCurrentUsername, String.class);
        // 自动填充更新时间
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime::now, LocalDateTime.class);
        // 自动填充更新人
        this.strictInsertFill(metaObject, "updateBy", this::getCurrentUsername, String.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 自动填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        // 自动填充更新人
        this.strictUpdateFill(metaObject, "updateBy", this::getCurrentUsername, String.class);
    }

    /**
     * 获取当前登录用户名
     *
     * @return 当前用户名，如果未登录则返回 "system"
     */
    private String getCurrentUsername() {
        try {
            if (StpUtil.isLogin()) {
                // 这里假设登录ID就是用户名，您可以根据实际情况调整
                // 例如：StpUtil.getLoginIdAsString() 或从 session 中获取更详细的用户信息
                return StpUtil.getLoginIdAsString();
            }
        } catch (Exception e) {
            log.warn("Failed to get current user for meta object handler, will use 'system'.", e);
        }
        return "system"; // 默认值
    }
}
