package com.myow.user.system.infrastructure.gateway;

import cn.hutool.core.collection.CollUtil;
import com.myow.common.port.PositionInfoPort;
import com.myow.user.system.infrastructure.persistence.po.PositionDO;
import com.myow.user.system.infrastructure.persistence.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SystemPositionInfoPort implements PositionInfoPort {

    private final PositionRepository positionRepository;

    @Override
    public Map<Long, String> getPositionNameMap(List<Long> positionIds) {
        if (CollUtil.isEmpty(positionIds)) {
            return Collections.emptyMap();
        }

        return positionRepository.listByIds(positionIds).stream()
                .collect(Collectors.toMap(PositionDO::getPositionId, PositionDO::getPositionName, (first, second) -> first));
    }
}
