package com.myow.user.system.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myow.common.response.PageResult;
import com.myow.common.response.Result;
import com.myow.user.system.application.dto.CreateRoleDeptReqDTO;
import com.myow.user.system.application.dto.PageRoleDeptReqDTO;
import com.myow.user.system.application.dto.RoleDeptIdsReqDTO;
import com.myow.user.system.application.dto.RoleDeptIdReqDTO;
import com.myow.user.system.application.dto.RoleDeptRespDTO;
import com.myow.user.system.application.dto.UpdateRoleDeptReqDTO;
import com.myow.user.system.application.service.RoleDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yss
 */
@Tag(name = "用户中心-角色部门数据范围", description = "角色与部门自定义数据范围的授权关系接口")
@RestController
@RequestMapping("/system/role-dept")
@RequiredArgsConstructor
public class RoleDeptController {

    private final RoleDeptService roleDeptService;

    @PostMapping("/create")
    @Operation(summary = "创建角色部门关联", description = "为角色新增一条自定义部门数据范围。")
    @SaCheckPermission("system:role:update")
    public Result<Boolean> createRoleDept(@RequestBody CreateRoleDeptReqDTO createReqDTO) {
        return Result.success(roleDeptService.createRoleDept(createReqDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新角色部门关联", description = "更新一条角色部门关联记录。通常建议使用批量保存接口维护自定义数据范围。")
    @SaCheckPermission("system:role:update")
    public Result<Boolean> updateRoleDept(@RequestBody @Validated UpdateRoleDeptReqDTO updateReqDTO) {
        roleDeptService.updateRoleDept(updateReqDTO);
        return Result.success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除角色部门关联", description = "删除角色与某个部门的数据范围授权关系。")
    @SaCheckPermission("system:role:update")
    public Result<Boolean> deleteRoleDept(@RequestBody @Validated RoleDeptIdReqDTO reqDTO) {
        roleDeptService.deleteRoleDept(reqDTO.getRoleId(), reqDTO.getDeptId());
        return Result.success(true);
    }

    @PostMapping("/get")
    @Operation(summary = "获取角色部门关联", description = "获取角色与指定部门的数据范围授权关系。")
    @SaCheckPermission("system:role:query")
    public Result<RoleDeptRespDTO> getRoleDept(@RequestBody @Validated RoleDeptIdReqDTO reqDTO) {
        return Result.success(roleDeptService.getRoleDept(reqDTO.getRoleId(), reqDTO.getDeptId()));
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询角色部门关联", description = "分页查询角色自定义部门数据范围关联明细。")
    @SaCheckPermission("system:role:query")
    public Result<PageResult<RoleDeptRespDTO>> getRoleDeptPage(@RequestBody PageRoleDeptReqDTO pageRoleDeptReqDTO) {
        return Result.success(roleDeptService.getRoleDeptPage(pageRoleDeptReqDTO));
    }

    @PostMapping("/ids")
    @Operation(summary = "查询角色自定义部门 ID", description = "根据角色 ID 返回自定义数据范围已勾选的部门 ID 列表，用于前端部门树回显。")
    @SaCheckPermission("system:role:query")
    public Result<List<Long>> listRoleDeptIds(@RequestBody @Validated RoleDeptIdsReqDTO reqDTO) {
        return Result.success(roleDeptService.listDeptIdsByRoleId(reqDTO.getRoleId()));
    }

    @PostMapping("/ids/save")
    @Operation(summary = "保存角色自定义部门数据范围", description = "一次性覆盖保存角色的自定义部门数据范围。仅当角色 dataScope 为自定义部门时生效。")
    @SaCheckPermission("system:role:update")
    public Result<Boolean> saveRoleDeptIds(@RequestBody @Validated RoleDeptIdsReqDTO reqDTO) {
        roleDeptService.saveRoleDepts(reqDTO.getRoleId(), reqDTO.getDeptIdList());
        return Result.success(true);
    }
}
