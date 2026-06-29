package com.myow.user.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.mybatis.util.MyPageUtil;
import com.myow.common.response.PageResult;
import com.myow.user.system.application.converter.LoginLogApplicationConverter;
import com.myow.user.system.application.dto.CreateLoginLogReqDTO;
import com.myow.user.system.application.dto.LoginLogRespDTO;
import com.myow.user.system.application.dto.PageLoginLogReqDTO;
import com.myow.user.system.domain.entity.LoginLog;
import com.myow.user.system.infrastructure.converter.LoginLogConverter;
import com.myow.user.system.infrastructure.persistence.po.LoginLogDO;
import com.myow.user.system.infrastructure.persistence.repository.LoginLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginLogService {

    private final LoginLogRepository loginLogRepository;
    private final LoginLogApplicationConverter loginLogApplicationConverter;
    private final LoginLogConverter loginLogConverter;

    public Long createLoginLog(CreateLoginLogReqDTO createReqDTO) {
        LoginLog loginLog = loginLogApplicationConverter.convert(createReqDTO);
        LoginLogDO loginLogDO = loginLogConverter.toDo(loginLog);
        if (loginLogDO.getLoginTime() == null) {
            loginLogDO.setLoginTime(LocalDateTime.now());
        }
        loginLogRepository.save(loginLogDO);
        return loginLogDO.getLoginLogId();
    }

    public LoginLogRespDTO getLoginLog(Long id) {
        return loginLogApplicationConverter.convert(loginLogRepository.getById(id));
    }

    public PageResult<LoginLogRespDTO> getLoginLogPage(PageLoginLogReqDTO pageLoginLogReqDTO) {
        Page<LoginLogDO> loginLogDOPage = loginLogRepository.selectPage(pageLoginLogReqDTO);
        if (loginLogDOPage == null || loginLogDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(loginLogDOPage, loginLogApplicationConverter::convert);
    }
}
