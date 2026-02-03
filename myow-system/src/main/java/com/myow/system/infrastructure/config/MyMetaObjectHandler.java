package com.myow.system.infrastructure.config;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.myow.system.domain.enums.SystemUserEnum;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充处理器
 *
 * @author yss
 * @since 2026-01-19
 */
@Component("metaObjectHandler")
public class MyMetaObjectHandler implements MetaObjectHandler {
    private static final Logger log = LoggerFactory.getLogger(MyMetaObjectHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        // 自动填充创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        // 自动填充创建人
        this.strictInsertFill(metaObject, "createBy", this::getCurrentLoginName, String.class);
        // 自动填充更新时间
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime::now, LocalDateTime.class);
        // 自动填充更新人
        this.strictInsertFill(metaObject, "updateTime", this::getCurrentLoginName, String.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 自动填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        // 自动填充更新人
        this.strictUpdateFill(metaObject, "updateBy", this::getCurrentLoginName, String.class);
    }

    /**
     * 获取当前登录用户名
     *
     * @return 当前用户名，如果未登录则返回 "system"
     */
    private String getCurrentLoginName() {
        try {
            if (StpUtil.isLogin()) {
                return StpUtil.getLoginIdAsString();
            }
        } catch (Exception e) {
            log.warn("Failed to get current user for meta object handler, will use 'system'.", e);
        }
        return SystemUserEnum.SYSTEM_USER.getCode();
    }
}
