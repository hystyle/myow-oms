package com.myow.infrastructure.persistence.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.infrastructure.persistence.entity.UserDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myow.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yss
 * @since 2026-01-19
 */
@Service
public class UserService extends ServiceImpl<UserMapper, UserDO> {

}
