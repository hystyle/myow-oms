package com.myow.user.system.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: yss
 * @date: 2026-01-27 22:50
 * @description:
 */
@Data
public class DepartmentTreeVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "部门id")
    private Long departmentId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "部门负责人姓名")
    private String managerName;

    @Schema(description = "部门负责人id")
    private Long managerId;

    @Schema(description = "父级部门id")
    private Long parentId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "同级上一个元素id")
    private Long preId;

    @Schema(description = "同级下一个元素id")
    private Long nextId;

    @Schema(description = "子部门")
    private List<DepartmentTreeVO> children;

    @Schema(description = "自己和所有递归子部门的id集合")
    private List<Long> selfAndAllChildrenIdList;
}
