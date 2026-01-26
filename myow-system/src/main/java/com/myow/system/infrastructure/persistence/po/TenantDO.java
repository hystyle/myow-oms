package com.myow.system.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 租户表
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_tenant")
public class TenantDO {

    /**
     * 主键ID
     */
    @TableId("tenant_id")
    private Long tenantId;

    /**
     * 租户编码（唯一业务标识）
     */
    private String tenantCode;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 租户套餐ID
     */
    private Long plansId;

    /**
     * 服务到期时间
     */
    private LocalDateTime expireTime;

    /**
     * 用户数量限制（-1表示不限制）
     */
    private Integer accountCount;

    /**
     * 状态：0-正常 1-停用
     */
    private String status;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 统一社会信用代码
     */
    private String licenseNumber;

    /**
     * 企业简介
     */
    private String intro;

    /**
     * 系统访问域名
     */
    private String domain;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标志：0-正常 1-已删除
     */
    private String deletedFlag;
}
