package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.DeptApplicationConverter;
import com.myow.system.application.dto.CreateDeptReqDTO;
import com.myow.system.application.dto.DeptRespDTO;
import com.myow.system.application.dto.PageDeptReqDTO;
import com.myow.system.application.dto.UpdateDeptReqDTO;
import com.myow.system.domain.entity.Dept;
import com.myow.system.infrastructure.converter.DeptConverter;
import com.myow.system.infrastructure.persistence.po.DeptDO;
import com.myow.system.infrastructure.persistence.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author yss
 */
@Service
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository deptRepository;
    private final DeptApplicationConverter deptApplicationConverter;
    private final DeptConverter deptConverter;

    public Long createDept(CreateDeptReqDTO createReqDTO) {
        Dept dept = deptApplicationConverter.convert(createReqDTO);
        deptRepository.save(deptConverter.toDo(dept));
        return dept.getDeptId();
    }

    public void updateDept(UpdateDeptReqDTO updateReqDTO) {
        Dept dept = deptApplicationConverter.convert(updateReqDTO);
        deptRepository.updateById(deptConverter.toDo(dept));
    }

    public void deleteDept(Long id) {
        deptRepository.removeById(id);
    }

    public DeptRespDTO getDept(Long id) {
        DeptDO deptDO = deptRepository.getById(id);
        return deptApplicationConverter.convert(deptConverter.toEntity(deptDO));
    }

    public PageResult<DeptRespDTO> getDeptPage(PageDeptReqDTO pageDeptReqDTO) {
        Page<DeptDO> deptDOPage = deptRepository.selectPage(pageDeptReqDTO);
        return MyPageUtil.of(deptDOPage, deptApplicationConverter::convert);
    }
}
