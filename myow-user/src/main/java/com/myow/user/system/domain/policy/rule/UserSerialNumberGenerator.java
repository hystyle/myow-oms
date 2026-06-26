package com.myow.user.system.domain.policy.rule;

import com.myow.common.support.serialnumber.SerialNumberGenerator;
import com.myow.common.support.serialnumber.SerialTemplate;
import com.myow.user.system.infrastructure.persistence.repository.UserRepository;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author: yss
 * @date: 2026-01-29 22:04
 * @description:
 */
@Component
public class UserSerialNumberGenerator implements SerialNumberGenerator {

    @Resource
    private UserRepository userRepository;

    @Override
    public SerialTemplate getTemplate() {
        return new UserSerialTemplate();
    }

    @Override
    @Nullable
    public String getLastNumber(String type, LocalDate businessDate) {
        Long maxUserNo = userRepository.getMaxUserNo();
        return maxUserNo + "";
    }

    // 如果默认的 parsePreviousSeq 不够用，可以覆盖
    @Override
    public Long parsePreviousSeq(String lastFullNo, SerialTemplate template) {
        // 自定义解析逻辑，例如取中间的数字部分
        if (lastFullNo == null) return null;
        // CG-202601-000001-DEPT01
        String[] parts = lastFullNo.split("-");
        if (parts.length >= 3) {
            try {
                return Long.parseLong(parts[2]);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }
}
