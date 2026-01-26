package com.myow.system.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author gemini
 */
@Getter
@Setter
public class Dept {
    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 负责人
     */
    private Long managerId;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String deletedFlag;

    /**
     * 创建部门
     */
    private Long createDept;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
