package com.myow.user.application.dto;

import com.myow.common.response.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageUserReqDTO extends PageParam {

    @Schema(description = "Keyword")
    @Length(max = 20, message = "keyword max length is 20")
    private String keyword;

    @Schema(description = "Department id")
    private Long deptId;

    @Schema(description = "User status")
    private String status;
}
