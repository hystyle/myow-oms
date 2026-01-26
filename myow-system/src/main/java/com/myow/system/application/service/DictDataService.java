package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.DictDataApplicationConverter;
import com.myow.system.application.dto.CreateDictDataReqDTO;
import com.myow.system.application.dto.DictDataRespDTO;
import com.myow.system.application.dto.PageDictDataReqDTO;
import com.myow.system.application.dto.UpdateDictDataReqDTO;
import com.myow.system.domain.entity.DictData;
import com.myow.system.infrastructure.converter.DictDataConverter;
import com.myow.system.infrastructure.persistence.po.DictDataDO;
import com.myow.system.infrastructure.persistence.repository.DictDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yss
 */
@Service
@RequiredArgsConstructor
public class DictDataService {

    private final DictDataRepository dictDataRepository;
    private final DictDataApplicationConverter dictDataApplicationConverter;
    private final DictDataConverter dictDataConverter;

    public Long createDictData(CreateDictDataReqDTO createReqDTO) {
        DictData dictData = dictDataApplicationConverter.convert(createReqDTO);
        dictDataRepository.save(dictDataConverter.toDo(dictData));
        return dictData.getDictDataId();
    }

    public void updateDictData(UpdateDictDataReqDTO updateReqDTO) {
        DictData dictData = dictDataApplicationConverter.convert(updateReqDTO);
        dictDataRepository.updateById(dictDataConverter.toDo(dictData));
    }

    public void deleteDictData(Long id) {
        dictDataRepository.removeById(id);
    }

    public DictDataRespDTO getDictData(Long id) {
        DictDataDO dictDataDO = dictDataRepository.getById(id);
        return dictDataApplicationConverter.convert(dictDataConverter.toEntity(dictDataDO));
    }

    public PageResult<DictDataRespDTO> getDictDataPage(PageDictDataReqDTO pageDictDataReqDTO) {
        Page<DictDataDO> dictDataDOPage = dictDataRepository.selectPage(pageDictDataReqDTO);
        if (Objects.isNull(dictDataDOPage) || dictDataDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(dictDataDOPage, dictDataApplicationConverter::convert);
    }
}
