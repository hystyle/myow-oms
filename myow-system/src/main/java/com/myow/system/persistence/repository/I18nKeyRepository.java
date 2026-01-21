package com.myow.system.persistence.repository;

import com.myow.system.persistence.po.I18nKeyDO;
import com.myow.system.persistence.mapper.I18nKeyMapper;
import com.myow.system.persistence.service.I18nKeyService;
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
public class I18nKeyRepository extends ServiceImpl<I18nKeyMapper, I18nKeyDO> implements I18nKeyService {

}
