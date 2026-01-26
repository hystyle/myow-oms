package com.myow.system.interfaces.controller;

import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.system.application.dto.CreateDeptReqDTO;
import com.myow.system.application.dto.DeptRespDTO;
import com.myow.system.application.dto.PageDeptReqDTO;
import com.myow.system.application.dto.UpdateDeptReqDTO;
import com.myow.system.application.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author gemini
 */
@Tag(name = "系统模块-部门")
@RestController
@RequestMapping("/system/dept")
@RequiredArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @PostMapping("/create")
    @Operation(summary = "创建部门")
    public Result<Long> createDept(@RequestBody CreateDeptReqDTO createReqDTO) {
        return Result.success(deptService.createDept(createReqDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新部门")
    public Result<Boolean> updateDept(@RequestBody UpdateDeptReqDTO updateReqDTO) {
        deptService.updateDept(updateReqDTO);
        return Result.success(true);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除部门")
    public Result<Boolean> deleteDept(@PathVariable("id") Long id) {
        deptService.deleteDept(id);
        return Result.success(true);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取部门")
    public Result<DeptRespDTO> getDept(@PathVariable("id") Long id) {
        return Result.success(deptService.getDept(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获取部门分页")
    public Result<PageResult<DeptRespDTO>> getDeptPage(PageDeptReqDTO pageDeptReqDTO) {
        return Result.success(deptService.getDeptPage(pageDeptReqDTO));
    }
}
