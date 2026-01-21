package com.myow.system.persistence.repository;

import com.myow.system.persistence.po.UserDO;
import com.myow.system.persistence.mapper.UserMapper;
import com.myow.system.persistence.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Service
public class UserRepository extends ServiceImpl<UserMapper, UserDO> implements UserService {

}
