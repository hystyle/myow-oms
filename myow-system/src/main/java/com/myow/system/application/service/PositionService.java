package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.PositionApplicationConverter;
import com.myow.system.application.dto.CreatePositionReqDTO;
import com.myow.system.application.dto.PagePositionReqDTO;
import com.myow.system.application.dto.PositionRespDTO;
import com.myow.system.application.dto.UpdatePositionReqDTO;
import com.myow.system.domain.entity.Position;
import com.myow.system.infrastructure.converter.PositionConverter;
import com.myow.system.infrastructure.persistence.po.PositionDO;
import com.myow.system.infrastructure.persistence.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author gemini
 */
@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final PositionApplicationConverter positionApplicationConverter;
    private final PositionConverter positionConverter;

    public Long createPosition(CreatePositionReqDTO createReqDTO) {
        Position position = positionApplicationConverter.convert(createReqDTO);
        positionRepository.save(positionConverter.toDo(position));
        return position.getPositionId();
    }

    public void updatePosition(UpdatePositionReqDTO updateReqDTO) {
        Position position = positionApplicationConverter.convert(updateReqDTO);
        positionRepository.updateById(positionConverter.toDo(position));
    }

    public void deletePosition(Long id) {
        positionRepository.removeById(id);
    }

    public PositionRespDTO getPosition(Long id) {
        PositionDO positionDO = positionRepository.getById(id);
        return positionApplicationConverter.convert(positionConverter.toEntity(positionDO));
    }

    public PageResult<PositionRespDTO> getPositionPage(PagePositionReqDTO pagePositionReqDTO) {
        Page<PositionDO> positionDOPage = positionRepository.selectPage(pagePositionReqDTO);
        if (Objects.isNull(positionDOPage) || positionDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(positionDOPage, positionApplicationConverter::convert);
    }
}
