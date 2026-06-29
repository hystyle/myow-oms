package com.myow.user.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PageLoginLogReqDTO extends PageParam {
    private Long tenantId;
    private Long userId;
    private String loginName;
    private Integer status;
    private LocalDateTime loginTimeStart;
    private LocalDateTime loginTimeEnd;
}
