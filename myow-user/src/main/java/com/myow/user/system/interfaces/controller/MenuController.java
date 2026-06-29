package com.myow.user.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateMenuReqDTO;
import com.myow.user.system.application.dto.IdReqDTO;
import com.myow.user.system.application.dto.MenuRespDTO;
import com.myow.user.system.application.dto.PageMenuReqDTO;
import com.myow.user.system.application.dto.UpdateMenuReqDTO;
import com.myow.user.system.application.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
 */
@Tag(name = "系统模块-菜单")
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/create")
    @Operation(summary = "创建菜单")
    @SaCheckPermission("system:menu:add")
    public Result<Long> createMenu(@RequestBody @Validated CreateMenuReqDTO createReqDTO) {
        return Result.success(menuService.createMenu(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新菜单")
    @SaCheckPermission("system:menu:update")
    public Result<Boolean> updateMenu(@RequestBody @Validated UpdateMenuReqDTO updateReqDTO) {
        menuService.updateMenu(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除菜单")
    @SaCheckPermission("system:menu:delete")
    public Result<Boolean> deleteMenu(@RequestBody @Validated IdReqDTO reqDTO) {
        menuService.deleteMenu(reqDTO.getId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取菜单")
    @SaCheckPermission("system:menu:query")
    public Result<MenuRespDTO> getMenu(@RequestBody @Validated IdReqDTO reqDTO) {
        return Result.success(menuService.getMenu(reqDTO.getId()));
    }

    @PostMapping("/page")
    @Operation(summary = "获取菜单分页")
    @SaCheckPermission("system:menu:query")
    public Result<PageResult<MenuRespDTO>> getMenuPage(@RequestBody PageMenuReqDTO pageMenuReqDTO) {
        return Result.success(menuService.getMenuPage(pageMenuReqDTO));
    }
}
