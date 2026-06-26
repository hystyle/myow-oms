package com.myow.user.system.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: yss
 * @date: 2026-02-04 22:12
 * @description:
 */
@Data
public class DepartmentVO {

    @Schema(description = "部门id")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;
}
