package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateMenuReqDTO;
import com.myow.system.application.dto.MenuRespDTO;
import com.myow.system.application.dto.PageMenuReqDTO;
import com.myow.system.application.dto.UpdateMenuReqDTO;
import com.myow.system.application.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author gemini
 */
@Tag(name = "系统模块-菜单")
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/create")
    @Operation(summary = "创建菜单")
    public Result<Long> createMenu(@RequestBody CreateMenuReqDTO createReqDTO) {
        return Result.success(menuService.createMenu(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新菜单")
    public Result<Boolean> updateMenu(@RequestBody UpdateMenuReqDTO updateReqDTO) {
        menuService.updateMenu(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除菜单")
    public Result<Boolean> deleteMenu(@PathVariable("id") Long id) {
        menuService.deleteMenu(id);
        return Result.success(true);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取菜单")
    public Result<MenuRespDTO> getMenu(@PathVariable("id") Long id) {
        return Result.success(menuService.getMenu(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获取菜单分页")
    public Result<PageResult<MenuRespDTO>> getMenuPage(PageMenuReqDTO pageMenuReqDTO) {
        return Result.success(menuService.getMenuPage(pageMenuReqDTO));
    }
}
