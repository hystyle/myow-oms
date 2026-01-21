package com.myow.system.persistence.repository;

import com.myow.system.persistence.po.I18nMessageDO;
import com.myow.system.persistence.mapper.I18nMessageMapper;
import com.myow.system.persistence.service.I18nMessageService;
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
public class I18nMessageRepository extends ServiceImpl<I18nMessageMapper, I18nMessageDO> implements I18nMessageService {

}
