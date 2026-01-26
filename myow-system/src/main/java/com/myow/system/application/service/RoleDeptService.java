package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.RoleDeptApplicationConverter;
import com.myow.system.application.dto.CreateRoleDeptReqDTO;
import com.myow.system.application.dto.PageRoleDeptReqDTO;
import com.myow.system.application.dto.RoleDeptRespDTO;
import com.myow.system.application.dto.UpdateRoleDeptReqDTO;
import com.myow.system.domain.entity.RoleDept;
import com.myow.system.infrastructure.converter.RoleDeptConverter;
import com.myow.system.infrastructure.persistence.po.RoleDeptDO;
import com.myow.system.infrastructure.persistence.repository.RoleDeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yss
 */
@Service
@RequiredArgsConstructor
public class RoleDeptService {

    private final RoleDeptRepository roleDeptRepository;
    private final RoleDeptApplicationConverter roleDeptApplicationConverter;
    private final RoleDeptConverter roleDeptConverter;

    public boolean createRoleDept(CreateRoleDeptReqDTO createReqDTO) {
        RoleDept roleDept = roleDeptApplicationConverter.convert(createReqDTO);
        return roleDeptRepository.save(roleDeptConverter.toDo(roleDept));
    }

    public void updateRoleDept(UpdateRoleDeptReqDTO updateReqDTO) {
        roleDeptRepository.removeByCompositeKey(updateReqDTO.getOriginalRoleId(), updateReqDTO.getOriginalDeptId());
        RoleDept newRoleDept = new RoleDept();
        newRoleDept.setRoleId(updateReqDTO.getRoleId());
        newRoleDept.setDeptId(updateReqDTO.getDeptId());
        roleDeptRepository.save(roleDeptConverter.toDo(newRoleDept));
    }

    public void deleteRoleDept(Long roleId, Long deptId) {
        roleDeptRepository.removeByCompositeKey(roleId, deptId);
    }

    public RoleDeptRespDTO getRoleDept(Long roleId, Long deptId) {
        RoleDeptDO roleDeptDO = roleDeptRepository.getByCompositeKey(roleId, deptId);
        return roleDeptApplicationConverter.convert(roleDeptConverter.toEntity(roleDeptDO));
    }

    public PageResult<RoleDeptRespDTO> getRoleDeptPage(PageRoleDeptReqDTO pageRoleDeptReqDTO) {
        Page<RoleDeptDO> roleDeptDOPage = roleDeptRepository.selectPage(pageRoleDeptReqDTO);
        if (Objects.isNull(roleDeptDOPage) || roleDeptDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }

        return MyPageUtil.of(roleDeptDOPage, roleDeptApplicationConverter::convert);
    }
}
