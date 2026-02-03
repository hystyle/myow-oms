package com.myow.system.application.vo;

import com.myow.system.application.dto.MenuRespDTO;
import com.myow.system.application.dto.UserRespDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author: yss
 * @date: 2026-02-03 21:28
 * @description: 登录结果
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginResultVO extends UserRespDTO {

    @Schema(description = "token")
    private String token;

    @Schema(description = "菜单和功能点清单")
    private List<MenuRespDTO> menuList;

}
