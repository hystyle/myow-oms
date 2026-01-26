package com.myow.system.application.dto;

import com.myow.common.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gemini
 */
@Getter
@Setter
public class PageUserPostReqDTO extends PageParam {
    private Long userId;
    private Long postId;
}
