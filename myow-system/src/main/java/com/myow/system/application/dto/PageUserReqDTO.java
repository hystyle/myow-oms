package com.myow.system.application.dto;

import com.myow.common.response.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * @author: yss
 * @date: 2026-01-26 19:28
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageUserReqDTO extends PageParam {

    @Schema(description = "搜索词")
    @Length(max = 20, message = "搜索词最多20字符")
    private String keyword;

    @Schema(description = "部门id")
    private Long deptId;

    @Schema(description = "用户状态")
    private String status;

}
