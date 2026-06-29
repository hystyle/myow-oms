package com.myow.user.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateDeptReqDTO;
import com.myow.user.system.application.dto.IdReqDTO;
import com.myow.user.system.application.dto.UpdateDeptReqDTO;
import com.myow.user.system.application.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yss
 */
@Tag(name = "系统模块-部门")
@RestController
@RequestMapping("/system/dept")
@RequiredArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @PostMapping("/create")
    @Operation(summary = "创建部门")
    @SaCheckPermission("system:dept:add")
    public Result<Long> createDept(@RequestBody @Validated CreateDeptReqDTO createReqDTO) {
        return Result.success(deptService.createDept(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新部门")
    @SaCheckPermission("system:dept:update")
    public Result<Boolean> updateDept(@RequestBody @Validated UpdateDeptReqDTO updateReqDTO) {
        deptService.updateDept(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除部门")
    @SaCheckPermission("system:dept:delete")
    public Result<Boolean> deleteDept(@RequestBody @Validated IdReqDTO reqDTO) {
        deptService.deleteDept(reqDTO.getId());
        return Result.success(true);
    }

    @PostMapping("/tree")
    @Operation(summary = "获取部门树")
    @SaCheckPermission("system:dept:query")
    public Result<List<?>> getDeptTree() {
        return Result.success(deptService.getDeptTree());
    }

    @PostMapping("/list")
    @Operation(summary = "获取部门列表")
    @SaCheckPermission("system:dept:query")
    public Result<?> getDeptList() {
        return Result.success(deptService.listAll());
    }
}
