package com.myow.system.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * @author yss
 */
@Getter
@Setter
public class UpdateDeptReqDTO {
    /**
     * 部门ID
     */
    @Schema(description = "部门id")
    @NotNull(message = "部门id不能为空")
    private Long deptId;

    /**
     * 父部门ID
     */
    @Schema(description = "上级部门id (可选)")
    private Long parentId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    @Length(min = 1, max = 50, message = "请输入正确的部门名称(1-50个字符)")
    @NotNull(message = "请输入正确的部门名称(1-50个字符)")
    private String deptName;

    /**
     * 显示顺序
     */
    @Schema(description = "排序")
    @NotNull(message = "排序值")
    private Integer sort;

    /**
     * 负责人
     */
    @Schema(description = "部门负责人id")
    private Long managerId;
}
