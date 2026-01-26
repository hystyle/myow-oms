package com.myow.system.application.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import com.myow.common.response.UserErrorCode;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.DictApplicationConverter;
import com.myow.system.application.dto.CreateDictReqDTO;
import com.myow.system.application.dto.DictRespDTO;
import com.myow.system.application.dto.PageDictReqDTO;
import com.myow.system.application.dto.UpdateDictReqDTO;
import com.myow.system.domain.entity.Dict;
import com.myow.system.infrastructure.converter.DictConverter;
import com.myow.system.infrastructure.persistence.po.DictDO;
import com.myow.system.infrastructure.persistence.po.DictDataDO;
import com.myow.system.infrastructure.persistence.repository.DictDataRepository;
import com.myow.system.infrastructure.persistence.repository.DictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DictService {

    private final DictRepository dictRepository;
    private final DictApplicationConverter dictApplicationConverter;
    private final DictConverter dictConverter;
    private final DictDataRepository dictDataRepository;

    public Long createDict(CreateDictReqDTO createReqDTO) {
        validateDictForCreate(createReqDTO);
        Dict dict = dictApplicationConverter.convert(createReqDTO);
        dictRepository.save(dictConverter.toDo(dict));
        return dict.getDictId();
    }

    public void updateDict(UpdateDictReqDTO updateReqDTO) {
        validateDictForUpdate(updateReqDTO);
        Dict dict = dictApplicationConverter.convert(updateReqDTO);
        dictRepository.updateById(dictConverter.toDo(dict));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteDict(Long id) {
        DictDO existDict = dictRepository.getById(id);
        if (Objects.isNull(existDict)) {
            throw new BusinessException(UserErrorCode.DICT_NOT_EXIST);
        }
        
        Long dataCount = dictDataRepository.count(Wrappers.lambdaQuery(DictDataDO.class)
            .eq(DictDataDO::getDictId, id));
        if (dataCount > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "该字典存在字典数据，无法删除");
        }
        
        dictRepository.removeById(id);
    }

    public DictRespDTO getDict(Long id) {
        DictDO dictDO = dictRepository.getById(id);
        if (Objects.isNull(dictDO)) {
            throw new BusinessException(UserErrorCode.DICT_NOT_EXIST);
        }
        return dictApplicationConverter.convert(dictConverter.toEntity(dictDO));
    }

    public PageResult<DictRespDTO> getDictPage(PageDictReqDTO pageDictReqDTO) {
        Page<DictDO> dictDOPage = dictRepository.selectPage(pageDictReqDTO);
        if (Objects.isNull(dictDOPage) || dictDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(dictDOPage, dictApplicationConverter::convert);
    }

    private void validateDictForCreate(CreateDictReqDTO createReqDTO) {
        if (StrUtil.isBlank(createReqDTO.getDictName())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "字典名称不能为空");
        }
        
        if (StrUtil.isBlank(createReqDTO.getDictCode())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "字典编码不能为空");
        }
        
        if (!createReqDTO.getDictCode().matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "字典编码格式不正确，只能包含字母、数字和下划线，且必须以字母开头");
        }
        
        Long countByName = dictRepository.count(Wrappers.lambdaQuery(DictDO.class)
            .eq(DictDO::getDictName, createReqDTO.getDictName()));
        if (countByName > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "字典名称已存在");
        }
        
        Long countByCode = dictRepository.count(Wrappers.lambdaQuery(DictDO.class)
            .eq(DictDO::getDictCode, createReqDTO.getDictCode()));
        if (countByCode > 0) {
            throw new BusinessException(UserErrorCode.ALREADY_EXIST, "字典编码已存在");
        }
    }

    private void validateDictForUpdate(UpdateDictReqDTO updateReqDTO) {
        if (Objects.isNull(updateReqDTO.getDictId())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "字典ID不能为空");
        }
        
        DictDO existDict = dictRepository.getById(updateReqDTO.getDictId());
        if (Objects.isNull(existDict)) {
            throw new BusinessException(UserErrorCode.DICT_NOT_EXIST);
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getDictCode())) {
            if (!updateReqDTO.getDictCode().matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "字典编码格式不正确，只能包含字母、数字和下划线，且必须以字母开头");
            }
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getDictName()) &&
            !Objects.equals(existDict.getDictName(), updateReqDTO.getDictName())) {
            Long countByName = dictRepository.count(Wrappers.lambdaQuery(DictDO.class)
                .eq(DictDO::getDictName, updateReqDTO.getDictName())
                .ne(DictDO::getDictId, updateReqDTO.getDictId()));
            if (countByName > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "字典名称已存在");
            }
        }
        
        if (StrUtil.isNotBlank(updateReqDTO.getDictCode()) &&
            !Objects.equals(existDict.getDictCode(), updateReqDTO.getDictCode())) {
            Long countByCode = dictRepository.count(Wrappers.lambdaQuery(DictDO.class)
                .eq(DictDO::getDictCode, updateReqDTO.getDictCode())
                .ne(DictDO::getDictId, updateReqDTO.getDictId()));
            if (countByCode > 0) {
                throw new BusinessException(UserErrorCode.ALREADY_EXIST, "字典编码已存在");
            }
        }
    }
}
