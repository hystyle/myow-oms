package com.myow.system.application.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.PositionApplicationConverter;
import com.myow.system.application.dto.CreatePositionReqDTO;
import com.myow.system.application.dto.PagePositionReqDTO;
import com.myow.system.application.dto.PositionRespDTO;
import com.myow.system.application.dto.UpdatePositionReqDTO;
import com.myow.system.domain.entity.Position;
import com.myow.system.infrastructure.converter.PositionConverter;
import com.myow.system.infrastructure.persistence.po.DeptDO;
import com.myow.system.infrastructure.persistence.po.PositionDO;
import com.myow.system.infrastructure.persistence.po.UserPostDO;
import com.myow.system.infrastructure.persistence.repository.DeptRepository;
import com.myow.system.infrastructure.persistence.repository.PositionRepository;
import com.myow.system.infrastructure.persistence.repository.UserPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final PositionApplicationConverter positionApplicationConverter;
    private final PositionConverter positionConverter;
    private final DeptRepository deptRepository;
    private final UserPostRepository userPostRepository;

    public Long createPosition(CreatePositionReqDTO createReqDTO) {
        validatePositionForCreate(createReqDTO);
        Position position = positionApplicationConverter.convert(createReqDTO);
        positionRepository.save(positionConverter.toDo(position));
        return position.getPositionId();
    }

    public void updatePosition(UpdatePositionReqDTO updateReqDTO) {
        validatePositionForUpdate(updateReqDTO);
        Position position = positionApplicationConverter.convert(updateReqDTO);
        positionRepository.updateById(positionConverter.toDo(position));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePosition(Long id) {
        PositionDO existPosition = positionRepository.getById(id);
        if (Objects.isNull(existPosition)) {
            throw new BusinessException(UserErrorCode.POSITION_NOT_EXIST);
        }
        
        Long userCount = userPostRepository.count(Wrappers.lambdaQuery(UserPostDO.class)
            .eq(UserPostDO::getPostId, id));
        if (userCount > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "该岗位已被用户使用，无法删除");
        }
        
        positionRepository.removeById(id);
    }

    public PositionRespDTO getPosition(Long id) {
        PositionDO positionDO = positionRepository.getById(id);
        if (Objects.isNull(positionDO)) {
            throw new BusinessException(UserErrorCode.POSITION_NOT_EXIST);
        }
        return positionApplicationConverter.convert(positionConverter.toEntity(positionDO));
    }

    public PageResult<PositionRespDTO> getPositionPage(PagePositionReqDTO pagePositionReqDTO) {
        Page<PositionDO> positionDOPage = positionRepository.selectPage(pagePositionReqDTO);
        if (Objects.isNull(positionDOPage) || positionDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(positionDOPage, positionApplicationConverter::convert);
    }

    private void validatePositionForCreate(CreatePositionReqDTO createReqDTO) {
        if (StrUtil.isBlank(createReqDTO.getPositionName())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "岗位名称不能为空");
        }
        
        if (StrUtil.isBlank(createReqDTO.getPositionCode())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "岗位编码不能为空");
        }
        
        if (!createReqDTO.getPositionCode().matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "岗位编码格式不正确，只能包含字母、数字和下划线，且必须以字母开头");
        }
        
        if (Objects.nonNull(createReqDTO.getStatus()) && 
            !Objects.equals(createReqDTO.getStatus(), "0") && 
            !Objects.equals(createReqDTO.getStatus(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "状态参数不正确");
        }
        
        if (Objects.nonNull(createReqDTO.getDeptId())) {
            DeptDO dept = deptRepository.getById(createReqDTO.getDeptId());
            if (Objects.isNull(dept)) {
                throw new BusinessException(UserErrorCode.DEPT_NOT_EXIST, "部门不存在");
            }
        }
        
        Long countByName = positionRepository.count(Wrappers.lambdaQuery(PositionDO.class)
            .eq(PositionDO::getPositionName, createReqDTO.getPositionName()));
        if (countByName > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "岗位名称已存在");
        }
        
        Long countByCode = positionRepository.count(Wrappers.lambdaQuery(PositionDO.class)
            .eq(PositionDO::getPositionCode, createReqDTO.getPositionCode()));
        if (countByCode > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "岗位编码已存在");
        }
    }

    private void validatePositionForUpdate(UpdatePositionReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getPositionId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "岗位ID不能为空");
        }
        
        PositionDO existPosition = positionRepository.getById(updateReqDTO.getPositionId());
        if (Objects.isNull(existPosition)) {
            throw new BusinessException(UserErrorCode.POSITION_NOT_EXIST);
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getPositionCode())) {
            if (!updateReqDTO.getPositionCode().matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "岗位编码格式不正确，只能包含字母、数字和下划线，且必须以字母开头");
            }
        }
        
        if (Objects.nonNull(updateReqDTO.getStatus()) && 
            !Objects.equals(updateReqDTO.getStatus(), "0") && 
            !Objects.equals(updateReqDTO.getStatus(), "1")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "状态参数不正确");
        }
        
        if (Objects.nonNull(updateReqDTO.getDeptId())) {
            DeptDO dept = deptRepository.getById(updateReqDTO.getDeptId());
            if (Objects.isNull(dept)) {
                throw new BusinessException(UserErrorCode.DEPT_NOT_EXIST, "部门不存在");
            }
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getPositionName()) && 
            !Objects.equals(existPosition.getPositionName(), updateReqDTO.getPositionName())) {
            Long countByName = positionRepository.count(Wrappers.lambdaQuery(PositionDO.class)
                .eq(PositionDO::getPositionName, updateReqDTO.getPositionName())
                .ne(PositionDO::getPositionId, updateReqDTO.getPositionId()));
            if (countByName > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "岗位名称已存在");
            }
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getPositionCode()) && 
            !Objects.equals(existPosition.getPositionCode(), updateReqDTO.getPositionCode())) {
            Long countByCode = positionRepository.count(Wrappers.lambdaQuery(PositionDO.class)
                .eq(PositionDO::getPositionCode, updateReqDTO.getPositionCode())
                .ne(PositionDO::getPositionId, updateReqDTO.getPositionId()));
            if (countByCode > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "岗位编码已存在");
            }
        }
    }
}
