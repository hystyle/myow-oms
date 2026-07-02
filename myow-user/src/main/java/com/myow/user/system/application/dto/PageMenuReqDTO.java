package com.myow.user.system.application.dto;

import com.myow.common.response.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
@Schema(description = "菜单分页查询请求")
public class PageMenuReqDTO extends PageParam {
    @Schema(description = "菜单名称，支持模糊查询", example = "用户管理")
    private String menuName;

    @Schema(description = "父菜单 ID", example = "0")
    private Long parentId;

    @Schema(description = "菜单类型，M=目录，C=菜单，F=按钮", example = "C")
    private String menuType;

    @Schema(description = "菜单状态，0=正常，1=停用", example = "0")
    private String status;
}
