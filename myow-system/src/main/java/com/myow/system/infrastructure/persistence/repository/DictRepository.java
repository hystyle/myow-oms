package com.myow.system.infrastructure.persistence.repository;

import com.myow.system.infrastructure.persistence.po.DictDO;
import com.myow.system.infrastructure.persistence.mapper.DictMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-26
 */
@Service
public class DictRepository extends ServiceImpl<DictMapper, DictDO> {

}
