package com.myow.user.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "User login result")
public class UserLoginResultVO {

    @Schema(description = "Token")
    private String token;

    private Long userId;

    private String tenantId;

    private String userCode;

    private String loginName;

    private String nickName;

    private String phone;

    private String email;

    private Boolean adminFlag;

    private List<UserMenuRespVO> menuList = List.of();
}
