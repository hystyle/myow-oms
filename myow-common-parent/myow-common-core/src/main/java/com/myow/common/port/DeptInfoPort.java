package com.myow.common.port;

import java.util.List;
import java.util.Map;

public interface DeptInfoPort {

    List<Long> listSelfAndChildrenIds(Long deptId);

    Map<Long, String> getDeptNameMap(List<Long> deptIds);
}
