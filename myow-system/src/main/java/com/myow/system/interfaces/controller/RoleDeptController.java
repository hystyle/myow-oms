package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateRoleDeptReqDTO;
import com.myow.system.application.dto.PageRoleDeptReqDTO;
import com.myow.system.application.dto.RoleDeptRespDTO;
import com.myow.system.application.dto.UpdateRoleDeptReqDTO;
import com.myow.system.application.service.RoleDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author gemini
 */
@Tag(name = "系统模块-角色部门关联")
@RestController
@RequestMapping("/system/role-dept")
@RequiredArgsConstructor
public class RoleDeptController {

    private final RoleDeptService roleDeptService;

    @PostMapping("/create")
    @Operation(summary = "创建角色部门关联")
    public Result<Boolean> createRoleDept(@RequestBody CreateRoleDeptReqDTO createReqDTO) {
        return Result.success(roleDeptService.createRoleDept(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新角色部门关联")
    public Result<Boolean> updateRoleDept(@RequestBody UpdateRoleDeptReqDTO updateReqDTO) {
        roleDeptService.updateRoleDept(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{roleId}/{deptId}")
    @Operation(summary = "删除角色部门关联")
    public Result<Boolean> deleteRoleDept(
            @PathVariable("roleId") Long roleId,
            @PathVariable("deptId") Long deptId) {
        roleDeptService.deleteRoleDept(roleId, deptId);
        return Result.success(true);
    }

    @GetMapping("/get/{roleId}/{deptId}")
    @Operation(summary = "获取角色部门关联")
    public Result<RoleDeptRespDTO> getRoleDept(
            @PathVariable("roleId") Long roleId,
            @PathVariable("deptId") Long deptId) {
        return Result.success(roleDeptService.getRoleDept(roleId, deptId));
    }

    @GetMapping("/page")
    @Operation(summary = "获取角色部门关联分页")
    public Result<PageResult<RoleDeptRespDTO>> getRoleDeptPage(PageRoleDeptReqDTO pageRoleDeptReqDTO) {
        return Result.success(roleDeptService.getRoleDeptPage(pageRoleDeptReqDTO));
    }
}
