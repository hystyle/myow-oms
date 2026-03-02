package com.myow.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateDeptReqDTO;
import com.myow.system.application.dto.UpdateDeptReqDTO;
import com.myow.system.application.service.DeptService;
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
    @SaCheckPermission("system:department:add")
    public Result<Long> createDept(@RequestBody @Validated CreateDeptReqDTO createReqDTO) {
        return Result.success(deptService.createDept(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新部门")
    @SaCheckPermission("system:department:edit")
    public Result<Boolean> updateDept(@RequestBody @Validated UpdateDeptReqDTO updateReqDTO) {
        deptService.updateDept(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除部门")
    @SaCheckPermission("system:department:delete")
    public Result<Boolean> deleteDept(@PathVariable("id") Long id) {
        deptService.deleteDept(id);
        return Result.success(true);
    }

    @GetMapping("/tree")
    @Operation(summary = "获取部门树")
    public Result<List<?>> getDeptTree() {
        return Result.success(deptService.getDeptTree());
    }

    @GetMapping("/list")
    @Operation(summary = "获取部门列表")
    public Result<?> getDeptList() {
        return Result.success(deptService.listAll());
    }
}
