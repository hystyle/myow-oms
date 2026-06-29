package com.myow.user.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageConfigReqDTO extends PageParam {
    private Long tenantId;
    private String configKey;
    private String groupCode;
}
