package com.myow.system.persistence.repository;

import com.myow.system.persistence.po.SerialNoRecordDO;
import com.myow.system.persistence.mapper.SerialNoRecordMapper;
import com.myow.system.persistence.service.SerialNoRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SerialNoRecordRepository extends ServiceImpl<SerialNoRecordMapper, SerialNoRecordDO> implements SerialNoRecordService {

}
