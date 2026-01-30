package com.myow.system.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageUserReqDTO;
import com.myow.system.infrastructure.persistence.mapper.UserMapper;
import com.myow.system.infrastructure.persistence.po.UserDO;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Repository
public class UserRepository extends ServiceImpl<UserMapper, UserDO> {

    public UserDO getByLoginName(String loginName) {
        return baseMapper.selectOne(Wrappers.<UserDO>lambdaQuery().eq(UserDO::getLoginName, loginName));
    }

    public UserDO getByPhone(String phone) {
        return baseMapper.selectOne(Wrappers.<UserDO>lambdaQuery().eq(UserDO::getPhone, phone));
    }

    public Page<UserDO> selectPage(PageUserReqDTO reqDTO, List<Long> departmentIdList) {
        Page<UserDO> page = MyPageUtil.convert2PageQuery(reqDTO, UserDO.class);
        return baseMapper.selectPage(page, reqDTO, departmentIdList);
    }

    public Long getMaxUserNo() {
        LambdaQueryWrapper<UserDO> lastQuery = Wrappers.lambdaQuery(UserDO.class)
                .select(UserDO::getUserId)
                .orderByDesc(UserDO::getUserId)
                .last("LIMIT 1");
        return baseMapper.selectOne(lastQuery).getUserId();
    }

    public UserDO getByEmail(String email) {
        return baseMapper.selectOne(Wrappers.<UserDO>lambdaQuery().eq(UserDO::getEmail, email));
    }

    public Map<String, Long> countInTenant(List<String> tenantIds) {
        if (CollUtil.isEmpty(tenantIds)) {
            return Collections.emptyMap();
        }

        // 使用 groupBy 进行分组统计
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("tenant_id", "COUNT(1) as user_count");
        queryWrapper.in("tenant_id", tenantIds);
        queryWrapper.groupBy("tenant_id");

        List<Map<String, Object>> result = baseMapper.selectMaps(queryWrapper);

        Map<String, Long> countMap = new HashMap<>();
        for (Map<String, Object> map : result) {
            String tenantId = String.valueOf(map.get("tenant_id"));
            Long count = ((Number) map.get("user_count")).longValue();
            countMap.put(tenantId, count);
        }

        return countMap;
    }
}
