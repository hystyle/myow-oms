package com.myow.user.application.dto;

import com.myow.common.response.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageUserReqDTO extends PageParam {

    @Schema(description = "关键词，支持按登录名、姓名、手机号等字段模糊查询", example = "admin")
    @Length(max = 20, message = "keyword max length is 20")
    private String keyword;

    @Schema(description = "部门 ID；传入后查询该部门及子部门下的用户", example = "100")
    private Long deptId;

    @Schema(description = "用户状态，true 表示启用，false 表示停用", example = "true")
    private String status;
}
