package com.myow.user.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.mybatis.util.MyPageUtil;
import com.myow.user.application.dto.PageUserReqDTO;
import com.myow.user.infrastructure.persistence.mapper.TenantUserMapper;
import com.myow.user.infrastructure.persistence.po.TenantUserDO;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TenantUserRepository extends ServiceImpl<TenantUserMapper, TenantUserDO> {

    public TenantUserDO getByLoginName(String loginName, Boolean deleteFlag) {
        return baseMapper.selectOne(Wrappers.<TenantUserDO>lambdaQuery()
                .eq(TenantUserDO::getLoginName, loginName)
                .eq(deleteFlag != null, TenantUserDO::getDeletedFlag, deleteFlag));
    }

    public TenantUserDO getByPhone(String phone) {
        return baseMapper.selectOne(Wrappers.<TenantUserDO>lambdaQuery().eq(TenantUserDO::getPhone, phone));
    }

    public TenantUserDO getByEmail(String email) {
        return baseMapper.selectOne(Wrappers.<TenantUserDO>lambdaQuery().eq(TenantUserDO::getEmail, email));
    }

    public Page<TenantUserDO> selectPage(PageUserReqDTO reqDTO, List<Long> departmentIdList) {
        Page<TenantUserDO> page = MyPageUtil.convert2PageQuery(reqDTO, TenantUserDO.class);
        LambdaQueryWrapper<TenantUserDO> queryWrapper = Wrappers.lambdaQuery(TenantUserDO.class)
                .in(CollUtil.isNotEmpty(departmentIdList), TenantUserDO::getDeptId, departmentIdList)
                .eq(StrUtil.isNotBlank(reqDTO.getStatus()), TenantUserDO::getStatus, Boolean.valueOf(reqDTO.getStatus()));

        if (StrUtil.isNotBlank(reqDTO.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper
                    .like(TenantUserDO::getNickName, reqDTO.getKeyword())
                    .or()
                    .like(TenantUserDO::getPhone, reqDTO.getKeyword())
                    .or()
                    .like(TenantUserDO::getLoginName, reqDTO.getKeyword()));
        }

        return baseMapper.selectPage(page, queryWrapper);
    }

    public Map<String, Long> countInTenant(List<String> tenantIds) {
        if (CollUtil.isEmpty(tenantIds)) {
            return Collections.emptyMap();
        }

        QueryWrapper<TenantUserDO> queryWrapper = new QueryWrapper<>();
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
