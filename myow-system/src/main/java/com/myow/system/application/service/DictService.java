package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.DictApplicationConverter;
import com.myow.system.application.dto.CreateDictReqDTO;
import com.myow.system.application.dto.DictRespDTO;
import com.myow.system.application.dto.PageDictReqDTO;
import com.myow.system.application.dto.UpdateDictReqDTO;
import com.myow.system.domain.entity.Dict;
import com.myow.system.infrastructure.converter.DictConverter;
import com.myow.system.infrastructure.persistence.po.DictDO;
import com.myow.system.infrastructure.persistence.repository.DictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yss
 */
@Service
@RequiredArgsConstructor
public class DictService {

    private final DictRepository dictRepository;
    private final DictApplicationConverter dictApplicationConverter;
    private final DictConverter dictConverter;

    public Long createDict(CreateDictReqDTO createReqDTO) {
        Dict dict = dictApplicationConverter.convert(createReqDTO);
        dictRepository.save(dictConverter.toDo(dict));
        return dict.getDictId();
    }

    public void updateDict(UpdateDictReqDTO updateReqDTO) {
        Dict dict = dictApplicationConverter.convert(updateReqDTO);
        dictRepository.updateById(dictConverter.toDo(dict));
    }

    public void deleteDict(Long id) {
        dictRepository.removeById(id);
    }

    public DictRespDTO getDict(Long id) {
        DictDO dictDO = dictRepository.getById(id);
        return dictApplicationConverter.convert(dictConverter.toEntity(dictDO));
    }

    public PageResult<DictRespDTO> getDictPage(PageDictReqDTO pageDictReqDTO) {
        Page<DictDO> dictDOPage = dictRepository.selectPage(pageDictReqDTO);
        if (Objects.isNull(dictDOPage) || dictDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(dictDOPage, dictApplicationConverter::convert);
    }
}
