package com.myow.user.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateRoleDeptReqDTO;
import com.myow.user.system.application.dto.PageRoleDeptReqDTO;
import com.myow.user.system.application.dto.RoleDeptIdReqDTO;
import com.myow.user.system.application.dto.RoleDeptRespDTO;
import com.myow.user.system.application.dto.UpdateRoleDeptReqDTO;
import com.myow.user.system.application.service.RoleDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yss
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

    @PostMapping("/update")
    @Operation(summary = "更新角色部门关联")
    public Result<Boolean> updateRoleDept(@RequestBody @Validated UpdateRoleDeptReqDTO updateReqDTO) {
        roleDeptService.updateRoleDept(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除角色部门关联")
    public Result<Boolean> deleteRoleDept(@RequestBody @Validated RoleDeptIdReqDTO reqDTO) {
        roleDeptService.deleteRoleDept(reqDTO.getRoleId(), reqDTO.getDeptId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取角色部门关联")
    public Result<RoleDeptRespDTO> getRoleDept(@RequestBody @Validated RoleDeptIdReqDTO reqDTO) {
        return Result.success(roleDeptService.getRoleDept(reqDTO.getRoleId(), reqDTO.getDeptId()));
    }

    @PostMapping("/page")
    @Operation(summary = "获取角色部门关联分页")
    public Result<PageResult<RoleDeptRespDTO>> getRoleDeptPage(@RequestBody PageRoleDeptReqDTO pageRoleDeptReqDTO) {
        return Result.success(roleDeptService.getRoleDeptPage(pageRoleDeptReqDTO));
    }
}
