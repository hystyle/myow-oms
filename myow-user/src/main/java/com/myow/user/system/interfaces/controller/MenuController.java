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
@Tag(name = "用户中心-菜单权限", description = "后台目录、菜单、按钮权限点和前端路由映射管理接口")
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/create")
    @Operation(summary = "创建菜单或权限点", description = "创建目录、菜单页面或按钮权限点。目录用于导航分组，菜单用于路由页面，按钮用于功能权限控制。")
    @SaCheckPermission("system:menu:add")
    public Result<Long> createMenu(@RequestBody @Validated CreateMenuReqDTO createReqDTO) {
        return Result.success(menuService.createMenu(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新菜单或权限点", description = "更新菜单名称、父级、路由、组件、权限码、显示状态、缓存状态和排序。")
    @SaCheckPermission("system:menu:update")
    public Result<Boolean> updateMenu(@RequestBody @Validated UpdateMenuReqDTO updateReqDTO) {
        menuService.updateMenu(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除菜单或权限点", description = "删除指定菜单或按钮权限点。删除前应确认是否存在子菜单和角色授权引用。")
    @SaCheckPermission("system:menu:delete")
    public Result<Boolean> deleteMenu(@RequestBody @Validated IdReqDTO reqDTO) {
        menuService.deleteMenu(reqDTO.getId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取菜单详情", description = "根据菜单 ID 获取目录、菜单或按钮权限点详情。")
    @SaCheckPermission("system:menu:query")
    public Result<MenuRespDTO> getMenu(@RequestBody @Validated IdReqDTO reqDTO) {
        return Result.success(menuService.getMenu(reqDTO.getId()));
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询菜单", description = "按菜单名称、父级、类型和状态查询菜单列表。前端可将返回结果组装为菜单树。")
    @SaCheckPermission("system:menu:query")
    public Result<PageResult<MenuRespDTO>> getMenuPage(@RequestBody PageMenuReqDTO pageMenuReqDTO) {
        return Result.success(menuService.getMenuPage(pageMenuReqDTO));
    }
}
