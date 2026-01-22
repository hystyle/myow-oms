package com.myow.system.persistence.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.persistence.mapper.PositionMapper;
import com.myow.system.persistence.po.PositionDO;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 岗位信息表 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Service
public class PositionRepository extends ServiceImpl<PositionMapper, PositionDO>  {

}
