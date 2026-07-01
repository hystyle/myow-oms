package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.system.infrastructure.persistence.mapper.NoticeUserMapper;
import com.myow.system.infrastructure.persistence.po.NoticeUserDO;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeUserRepository extends ServiceImpl<NoticeUserMapper, NoticeUserDO> {
}
