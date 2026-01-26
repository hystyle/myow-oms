package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.RoleApplicationConverter;
import com.myow.system.application.dto.CreateRoleReqDTO;
import com.myow.system.application.dto.PageRoleReqDTO;
import com.myow.system.application.dto.RoleRespDTO;
import com.myow.system.application.dto.UpdateRoleReqDTO;
import com.myow.system.domain.entity.Role;
import com.myow.system.infrastructure.converter.RoleConverter;
import com.myow.system.infrastructure.persistence.po.RoleDO;
import com.myow.system.infrastructure.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author gemini
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleApplicationConverter roleApplicationConverter;
    private final RoleConverter roleConverter;

    public Long createRole(CreateRoleReqDTO createReqDTO) {
        Role role = roleApplicationConverter.convert(createReqDTO);
        roleRepository.save(roleConverter.toDo(role));
        return role.getRoleId();
    }

    public void updateRole(UpdateRoleReqDTO updateReqDTO) {
        Role role = roleApplicationConverter.convert(updateReqDTO);
        roleRepository.updateById(roleConverter.toDo(role));
    }

    public void deleteRole(Long id) {
        roleRepository.removeById(id);
    }

    public RoleRespDTO getRole(Long id) {
        RoleDO roleDO = roleRepository.getById(id);
        return roleApplicationConverter.convert(roleConverter.toEntity(roleDO));
    }

    public PageResult<RoleRespDTO> getRolePage(PageRoleReqDTO pageRoleReqDTO) {
        Page<RoleDO> roleDOPage = roleRepository.selectPage(pageRoleReqDTO);
        if (Objects.isNull(roleDOPage) || roleDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(roleDOPage, roleApplicationConverter::convert);
    }
}
