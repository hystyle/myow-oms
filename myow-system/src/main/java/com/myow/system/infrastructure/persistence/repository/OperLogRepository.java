package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.po.OperLogDO;
import com.myow.system.infrastructure.persistence.mapper.OperLogMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Service
public class OperLogRepository extends ServiceImpl<OperLogMapper, OperLogDO>  {

}
