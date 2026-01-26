package com.myow.system.application.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yss
 */
@Getter
@Setter
public class UpdateUserPostReqDTO {
    /**
     * Original User ID (for identifying the record to update)
     */
    private Long originalUserId;

    /**
     * Original Post ID (for identifying the record to update)
     */
    private Long originalPostId;

    /**
     * New User ID
     */
    private Long userId;

    /**
     * New Post ID
     */
    private Long postId;
}
