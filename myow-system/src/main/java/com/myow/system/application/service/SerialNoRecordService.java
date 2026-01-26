package com.myow.system.application.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.SerialNoRecordApplicationConverter;
import com.myow.system.application.dto.CreateSerialNoRecordReqDTO;
import com.myow.system.application.dto.PageSerialNoRecordReqDTO;
import com.myow.system.application.dto.SerialNoRecordRespDTO;
import com.myow.system.application.dto.UpdateSerialNoRecordReqDTO;
import com.myow.system.domain.entity.SerialNoRecord;
import com.myow.system.infrastructure.converter.SerialNoRecordConverter;
import com.myow.system.infrastructure.persistence.po.SerialNoRecordDO;
import com.myow.system.infrastructure.persistence.repository.SerialNoRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author gemini
 */
@Service
@RequiredArgsConstructor
public class SerialNoRecordService {

    private final SerialNoRecordRepository serialNoRecordRepository;
    private final SerialNoRecordApplicationConverter serialNoRecordApplicationConverter;
    private final SerialNoRecordConverter serialNoRecordConverter;

    public Integer createSerialNoRecord(CreateSerialNoRecordReqDTO createReqDTO) {
        SerialNoRecord serialNoRecord = serialNoRecordApplicationConverter.convert(createReqDTO);
        serialNoRecordRepository.save(serialNoRecordConverter.toDo(serialNoRecord));
        return serialNoRecord.getSerialNumberId();
    }

    public void updateSerialNoRecord(UpdateSerialNoRecordReqDTO updateReqDTO) {
        SerialNoRecord serialNoRecord = serialNoRecordApplicationConverter.convert(updateReqDTO);
        serialNoRecordRepository.updateById(serialNoRecordConverter.toDo(serialNoRecord));
    }

    public void deleteSerialNoRecord(Integer serialNumberId, LocalDate recordDate) {
        LambdaQueryWrapper<SerialNoRecordDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SerialNoRecordDO::getSerialNumberId, serialNumberId);
        queryWrapper.eq(SerialNoRecordDO::getRecordDate, recordDate);
        serialNoRecordRepository.remove(queryWrapper);
    }

    public SerialNoRecordRespDTO getSerialNoRecord(Integer serialNumberId, LocalDate recordDate) {
        LambdaQueryWrapper<SerialNoRecordDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SerialNoRecordDO::getSerialNumberId, serialNumberId);
        queryWrapper.eq(SerialNoRecordDO::getRecordDate, recordDate);
        SerialNoRecordDO serialNoRecordDO = serialNoRecordRepository.getOne(queryWrapper);
        return serialNoRecordApplicationConverter.convert(serialNoRecordConverter.toEntity(serialNoRecordDO));
    }

    public PageResult<SerialNoRecordRespDTO> getSerialNoRecordPage(PageSerialNoRecordReqDTO pageSerialNoRecordReqDTO) {
        Page<SerialNoRecordDO> serialNoRecordDOPage = serialNoRecordRepository.selectPage(pageSerialNoRecordReqDTO);
        if (Objects.isNull(serialNoRecordDOPage) || serialNoRecordDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(serialNoRecordDOPage, serialNoRecordApplicationConverter::convert);
    }
}
