package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.po.I18nMessageDO;
import com.myow.system.infrastructure.persistence.mapper.I18nMessageMapper;
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
public class I18nMessageRepository extends ServiceImpl<I18nMessageMapper, I18nMessageDO> {

}
