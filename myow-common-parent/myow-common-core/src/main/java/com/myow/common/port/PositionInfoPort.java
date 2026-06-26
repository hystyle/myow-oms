package com.myow.common.port;

import java.util.List;
import java.util.Map;

public interface PositionInfoPort {

    Map<Long, String> getPositionNameMap(List<Long> positionIds);
}
