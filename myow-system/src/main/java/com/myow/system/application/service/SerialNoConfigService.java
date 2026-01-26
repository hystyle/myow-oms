package com.myow.system.application.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.SerialNoConfigApplicationConverter;
import com.myow.system.application.dto.CreateSerialNoConfigReqDTO;
import com.myow.system.application.dto.PageSerialNoConfigReqDTO;
import com.myow.system.application.dto.SerialNoConfigRespDTO;
import com.myow.system.application.dto.UpdateSerialNoConfigReqDTO;
import com.myow.system.domain.entity.SerialNoConfig;
import com.myow.system.infrastructure.converter.SerialNoConfigConverter;
import com.myow.system.infrastructure.persistence.po.SerialNoConfigDO;
import com.myow.system.infrastructure.persistence.po.SerialNoRecordDO;
import com.myow.system.infrastructure.persistence.repository.SerialNoConfigRepository;
import com.myow.system.infrastructure.persistence.repository.SerialNoRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SerialNoConfigService {

    private final SerialNoConfigRepository serialNoConfigRepository;
    private final SerialNoConfigApplicationConverter serialNoConfigApplicationConverter;
    private final SerialNoConfigConverter serialNoConfigConverter;
    private final SerialNoRecordRepository serialNoRecordRepository;

    public Integer createSerialNoConfig(CreateSerialNoConfigReqDTO createReqDTO) {
        validateSerialNoConfigForCreate(createReqDTO);
        SerialNoConfig serialNoConfig = serialNoConfigApplicationConverter.convert(createReqDTO);
        serialNoConfigRepository.save(serialNoConfigConverter.toDo(serialNoConfig));
        return serialNoConfig.getSerialNumberId();
    }

    public void updateSerialNoConfig(UpdateSerialNoConfigReqDTO updateReqDTO) {
        validateSerialNoConfigForUpdate(updateReqDTO);
        SerialNoConfig serialNoConfig = serialNoConfigApplicationConverter.convert(updateReqDTO);
        serialNoConfigRepository.updateById(serialNoConfigConverter.toDo(serialNoConfig));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteSerialNoConfig(Integer id) {
        SerialNoConfigDO existConfig = serialNoConfigRepository.getById(id);
        if (Objects.isNull(existConfig)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "序列号配置不存在");
        }
        
        Long recordCount = serialNoRecordRepository.count(Wrappers.lambdaQuery(SerialNoRecordDO.class)
            .eq(SerialNoRecordDO::getSerialNumberId, id));
        if (recordCount > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "该序列号配置存在记录，无法删除");
        }
        
        serialNoConfigRepository.removeById(id);
    }

    public SerialNoConfigRespDTO getSerialNoConfig(Integer id) {
        SerialNoConfigDO serialNoConfigDO = serialNoConfigRepository.getById(id);
        if (Objects.isNull(serialNoConfigDO)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "序列号配置不存在");
        }
        return serialNoConfigApplicationConverter.convert(serialNoConfigConverter.toEntity(serialNoConfigDO));
    }

    public PageResult<SerialNoConfigRespDTO> getSerialNoConfigPage(PageSerialNoConfigReqDTO pageSerialNoConfigReqDTO) {
        Page<SerialNoConfigDO> serialNoConfigDOPage = serialNoConfigRepository.selectPage(pageSerialNoConfigReqDTO);
        if (Objects.isNull(serialNoConfigDOPage) || serialNoConfigDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(serialNoConfigDOPage, serialNoConfigApplicationConverter::convert);
    }

    private void validateSerialNoConfigForCreate(CreateSerialNoConfigReqDTO createReqDTO) {
        if (StrUtil.isBlank(createReqDTO.getBusinessName())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "业务名称不能为空");
        }
        
        if (StrUtil.isBlank(createReqDTO.getFormat())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "序列号格式不能为空");
        }
        
        if (StrUtil.isBlank(createReqDTO.getRuleType())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "规则类型不能为空");
        }
        
        if (Objects.nonNull(createReqDTO.getInitNumber()) && createReqDTO.getInitNumber() < 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "初始号码不能小于0");
        }
        
        if (Objects.nonNull(createReqDTO.getStepRandomRange()) && createReqDTO.getStepRandomRange() < 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "步长随机范围不能小于0");
        }
        
        Long countByBusinessName = serialNoConfigRepository.count(Wrappers.lambdaQuery(SerialNoConfigDO.class)
            .eq(SerialNoConfigDO::getBusinessName, createReqDTO.getBusinessName()));
        if (countByBusinessName > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "业务名称已存在");
        }
    }

    private void validateSerialNoConfigForUpdate(UpdateSerialNoConfigReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getSerialNumberId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "序列号配置ID不能为空");
        }
        
        SerialNoConfigDO existConfig = serialNoConfigRepository.getById(updateReqDTO.getSerialNumberId());
        if (Objects.isNull(existConfig)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "序列号配置不存在");
        }
        
        if (Objects.nonNull(updateReqDTO.getInitNumber()) && updateReqDTO.getInitNumber() < 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "初始号码不能小于0");
        }
        
        if (Objects.nonNull(updateReqDTO.getStepRandomRange()) && updateReqDTO.getStepRandomRange() < 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "步长随机范围不能小于0");
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getBusinessName()) && 
            !Objects.equals(existConfig.getBusinessName(), updateReqDTO.getBusinessName())) {
            Long countByBusinessName = serialNoConfigRepository.count(Wrappers.lambdaQuery(SerialNoConfigDO.class)
                .eq(SerialNoConfigDO::getBusinessName, updateReqDTO.getBusinessName())
                .ne(SerialNoConfigDO::getSerialNumberId, updateReqDTO.getSerialNumberId()));
            if (countByBusinessName > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "业务名称已存在");
            }
        }
    }
}
