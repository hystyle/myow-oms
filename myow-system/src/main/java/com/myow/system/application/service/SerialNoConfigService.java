package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.SerialNoConfigApplicationConverter;
import com.myow.system.application.dto.CreateSerialNoConfigReqDTO;
import com.myow.system.application.dto.PageSerialNoConfigReqDTO;
import com.myow.system.application.dto.SerialNoConfigRespDTO;
import com.myow.system.application.dto.UpdateSerialNoConfigReqDTO;
import com.myow.system.domain.entity.SerialNoConfig;
import com.myow.system.infrastructure.converter.SerialNoConfigConverter;
import com.myow.system.infrastructure.persistence.po.SerialNoConfigDO;
import com.myow.system.infrastructure.persistence.repository.SerialNoConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yss
 */
@Service
@RequiredArgsConstructor
public class SerialNoConfigService {

    private final SerialNoConfigRepository serialNoConfigRepository;
    private final SerialNoConfigApplicationConverter serialNoConfigApplicationConverter;
    private final SerialNoConfigConverter serialNoConfigConverter;

    public Integer createSerialNoConfig(CreateSerialNoConfigReqDTO createReqDTO) {
        SerialNoConfig serialNoConfig = serialNoConfigApplicationConverter.convert(createReqDTO);
        serialNoConfigRepository.save(serialNoConfigConverter.toDo(serialNoConfig));
        return serialNoConfig.getSerialNumberId();
    }

    public void updateSerialNoConfig(UpdateSerialNoConfigReqDTO updateReqDTO) {
        SerialNoConfig serialNoConfig = serialNoConfigApplicationConverter.convert(updateReqDTO);
        serialNoConfigRepository.updateById(serialNoConfigConverter.toDo(serialNoConfig));
    }

    public void deleteSerialNoConfig(Integer id) {
        serialNoConfigRepository.removeById(id);
    }

    public SerialNoConfigRespDTO getSerialNoConfig(Integer id) {
        SerialNoConfigDO serialNoConfigDO = serialNoConfigRepository.getById(id);
        return serialNoConfigApplicationConverter.convert(serialNoConfigConverter.toEntity(serialNoConfigDO));
    }

    public PageResult<SerialNoConfigRespDTO> getSerialNoConfigPage(PageSerialNoConfigReqDTO pageSerialNoConfigReqDTO) {
        Page<SerialNoConfigDO> serialNoConfigDOPage = serialNoConfigRepository.selectPage(pageSerialNoConfigReqDTO);
        if (Objects.isNull(serialNoConfigDOPage) || serialNoConfigDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(serialNoConfigDOPage, serialNoConfigApplicationConverter::convert);
    }
}
