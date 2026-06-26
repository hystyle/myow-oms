package com.myow.user.system.infrastructure.gateway;

import cn.hutool.core.collection.CollUtil;
import com.myow.common.port.DeptInfoPort;
import com.myow.user.system.application.service.DeptService;
import com.myow.user.system.infrastructure.persistence.po.DeptDO;
import com.myow.user.system.infrastructure.persistence.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SystemDeptInfoPort implements DeptInfoPort {

    private final DeptService deptService;
    private final DeptRepository deptRepository;

    @Override
    public List<Long> listSelfAndChildrenIds(Long deptId) {
        if (deptId == null) {
            return Collections.emptyList();
        }
        return deptService.selfAndChildrenIdList(deptId);
    }

    @Override
    public Map<Long, String> getDeptNameMap(List<Long> deptIds) {
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyMap();
        }

        return deptRepository.listByIds(deptIds).stream()
                .collect(Collectors.toMap(DeptDO::getDeptId, DeptDO::getDeptName, (first, second) -> first));
    }
}
