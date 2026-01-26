package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.po.SerialNoRecordDO;
import com.myow.system.infrastructure.persistence.mapper.SerialNoRecordMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流水号记录表 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Service
public class SerialNoRecordRepository extends ServiceImpl<SerialNoRecordMapper, SerialNoRecordDO> {

}
