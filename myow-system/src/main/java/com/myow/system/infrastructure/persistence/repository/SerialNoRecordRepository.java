package com.myow.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.dto.PageSerialNoRecordReqDTO; // Need to create this DTO later
import com.myow.system.infrastructure.persistence.mapper.SerialNoRecordMapper;
import com.myow.system.infrastructure.persistence.po.SerialNoRecordDO;
import org.springframework.stereotype.Repository;

/**
 * @author yss
 */
@Repository
public class SerialNoRecordRepository extends ServiceImpl<SerialNoRecordMapper, SerialNoRecordDO> {

    public Page<SerialNoRecordDO> selectPage(PageSerialNoRecordReqDTO reqDTO) {
        Page<SerialNoRecordDO> page = MyPageUtil.convert2PageQuery(reqDTO, SerialNoRecordDO.class);
        LambdaQueryWrapper<SerialNoRecordDO> queryWrapper = Wrappers.lambdaQuery();

        if (reqDTO.getSerialNumberId() != null) {
            queryWrapper.eq(SerialNoRecordDO::getSerialNumberId, reqDTO.getSerialNumberId());
        }
        if (reqDTO.getRecordDate() != null) {
            queryWrapper.eq(SerialNoRecordDO::getRecordDate, reqDTO.getRecordDate());
        }
        // Add more query conditions as needed based on PageSerialNoRecordReqDTO fields

        return this.page(page, queryWrapper);
    }
}