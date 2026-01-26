package com.myow.system.application.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.DictDataApplicationConverter;
import com.myow.system.application.dto.CreateDictDataReqDTO;
import com.myow.system.application.dto.DictDataRespDTO;
import com.myow.system.application.dto.PageDictDataReqDTO;
import com.myow.system.application.dto.UpdateDictDataReqDTO;
import com.myow.system.domain.entity.DictData;
import com.myow.system.infrastructure.converter.DictDataConverter;
import com.myow.system.infrastructure.persistence.po.DictDataDO;
import com.myow.system.infrastructure.persistence.po.DictDO;
import com.myow.system.infrastructure.persistence.repository.DictDataRepository;
import com.myow.system.infrastructure.persistence.repository.DictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DictDataService {

    private final DictDataRepository dictDataRepository;
    private final DictDataApplicationConverter dictDataApplicationConverter;
    private final DictDataConverter dictDataConverter;
    private final DictRepository dictRepository;

    public Long createDictData(CreateDictDataReqDTO createReqDTO) {
        validateDictDataForCreate(createReqDTO);
        DictData dictData = dictDataApplicationConverter.convert(createReqDTO);
        dictDataRepository.save(dictDataConverter.toDo(dictData));
        return dictData.getDictDataId();
    }

    public void updateDictData(UpdateDictDataReqDTO updateReqDTO) {
        validateDictDataForUpdate(updateReqDTO);
        DictData dictData = dictDataApplicationConverter.convert(updateReqDTO);
        dictDataRepository.updateById(dictDataConverter.toDo(dictData));
    }

    public void deleteDictData(Long id) {
        DictDataDO existDictData = dictDataRepository.getById(id);
        if (Objects.isNull(existDictData)) {
            throw new BusinessException(UserErrorCode.DICT_NOT_EXIST, "字典数据不存在");
        }
        dictDataRepository.removeById(id);
    }

    public DictDataRespDTO getDictData(Long id) {
        DictDataDO dictDataDO = dictDataRepository.getById(id);
        if (Objects.isNull(dictDataDO)) {
            throw new BusinessException(UserErrorCode.DICT_NOT_EXIST, "字典数据不存在");
        }
        return dictDataApplicationConverter.convert(dictDataConverter.toEntity(dictDataDO));
    }

    public PageResult<DictDataRespDTO> getDictDataPage(PageDictDataReqDTO pageDictDataReqDTO) {
        Page<DictDataDO> dictDataDOPage = dictDataRepository.selectPage(pageDictDataReqDTO);
        if (Objects.isNull(dictDataDOPage) || dictDataDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(dictDataDOPage, dictDataApplicationConverter::convert);
    }

    private void validateDictDataForCreate(CreateDictDataReqDTO createReqDTO) {
        if (Objects.isNull(createReqDTO.getDictId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "字典ID不能为空");
        }
        
        DictDO dict = dictRepository.getById(createReqDTO.getDictId());
        if (Objects.isNull(dict)) {
            throw new BusinessException(UserErrorCode.DICT_NOT_EXIST, "字典不存在");
        }
        
        if (StrUtil.isBlank(createReqDTO.getDataLabel())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "字典标签不能为空");
        }
        
        if (StrUtil.isBlank(createReqDTO.getDataValue())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "字典值不能为空");
        }
        
        Long countByLabel = dictDataRepository.count(Wrappers.lambdaQuery(DictDataDO.class)
            .eq(DictDataDO::getDictId, createReqDTO.getDictId())
            .eq(DictDataDO::getDataLabel, createReqDTO.getDataLabel()));
        if (countByLabel > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "同一字典下字典标签已存在");
        }
        
        Long countByValue = dictDataRepository.count(Wrappers.lambdaQuery(DictDataDO.class)
            .eq(DictDataDO::getDictId, createReqDTO.getDictId())
            .eq(DictDataDO::getDataValue, createReqDTO.getDataValue()));
        if (countByValue > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "同一字典下字典值已存在");
        }
    }

    private void validateDictDataForUpdate(UpdateDictDataReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getDictDataId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "字典数据ID不能为空");
        }
        
        DictDataDO existDictData = dictDataRepository.getById(updateReqDTO.getDictDataId());
        if (Objects.isNull(existDictData)) {
            throw new BusinessException(UserErrorCode.DICT_NOT_EXIST, "字典数据不存在");
        }
        
        if (Objects.nonNull(updateReqDTO.getDictId())) {
            DictDO dict = dictRepository.getById(updateReqDTO.getDictId());
            if (Objects.isNull(dict)) {
                throw new BusinessException(UserErrorCode.DICT_NOT_EXIST, "字典不存在");
            }
        }
        
        Long dictId = Objects.nonNull(updateReqDTO.getDictId()) ? 
            updateReqDTO.getDictId() : existDictData.getDictId();
        
        if (StrUtil.isNotBlank(updateReqDTO.getDataLabel()) && 
            !Objects.equals(existDictData.getDataLabel(), updateReqDTO.getDataLabel())) {
            Long countByLabel = dictDataRepository.count(Wrappers.lambdaQuery(DictDataDO.class)
                .eq(DictDataDO::getDictId, dictId)
                .eq(DictDataDO::getDataLabel, updateReqDTO.getDataLabel())
                .ne(DictDataDO::getDictDataId, updateReqDTO.getDictDataId()));
            if (countByLabel > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "同一字典下字典标签已存在");
            }
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getDataValue()) && 
            !Objects.equals(existDictData.getDataValue(), updateReqDTO.getDataValue())) {
            Long countByValue = dictDataRepository.count(Wrappers.lambdaQuery(DictDataDO.class)
                .eq(DictDataDO::getDictId, dictId)
                .eq(DictDataDO::getDataValue, updateReqDTO.getDataValue())
                .ne(DictDataDO::getDictDataId, updateReqDTO.getDictDataId()));
            if (countByValue > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "同一字典下字典值已存在");
            }
        }
    }
}
