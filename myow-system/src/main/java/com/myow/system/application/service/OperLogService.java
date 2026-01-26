package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.OperLogApplicationConverter;
import com.myow.system.application.dto.CreateOperLogReqDTO;
import com.myow.system.application.dto.OperLogRespDTO;
import com.myow.system.application.dto.PageOperLogReqDTO;
import com.myow.system.application.dto.UpdateOperLogReqDTO;
import com.myow.system.domain.entity.OperLog;
import com.myow.system.infrastructure.converter.OperLogConverter;
import com.myow.system.infrastructure.persistence.po.OperLogDO;
import com.myow.system.infrastructure.persistence.repository.OperLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author gemini
 */
@Service
@RequiredArgsConstructor
public class OperLogService {

    private final OperLogRepository operLogRepository;
    private final OperLogApplicationConverter operLogApplicationConverter;
    private final OperLogConverter operLogConverter;

    public Long createOperLog(CreateOperLogReqDTO createReqDTO) {
        OperLog operLog = operLogApplicationConverter.convert(createReqDTO);
        operLogRepository.save(operLogConverter.toDo(operLog));
        return operLog.getOperId();
    }

    public void updateOperLog(UpdateOperLogReqDTO updateReqDTO) {
        OperLog operLog = operLogApplicationConverter.convert(updateReqDTO);
        operLogRepository.updateById(operLogConverter.toDo(operLog));
    }

    public void deleteOperLog(Long id) {
        operLogRepository.removeById(id);
    }

    public OperLogRespDTO getOperLog(Long id) {
        OperLogDO operLogDO = operLogRepository.getById(id);
        return operLogApplicationConverter.convert(operLogConverter.toEntity(operLogDO));
    }

    public PageResult<OperLogRespDTO> getOperLogPage(PageOperLogReqDTO pageOperLogReqDTO) {
        Page<OperLogDO> operLogDOPage = operLogRepository.selectPage(pageOperLogReqDTO);
        if (Objects.isNull(operLogDOPage) || operLogDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(operLogDOPage, operLogApplicationConverter::convert);
    }
}
