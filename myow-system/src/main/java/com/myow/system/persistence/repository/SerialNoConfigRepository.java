package com.myow.system.persistence.repository;

import com.myow.system.persistence.po.SerialNoConfigDO;
import com.myow.system.persistence.mapper.SerialNoConfigMapper;
import com.myow.system.persistence.service.SerialNoConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Service
public class SerialNoConfigRepository extends ServiceImpl<SerialNoConfigMapper, SerialNoConfigDO> implements SerialNoConfigService {

}
