package com.myow.user.application.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSessionRespVO {

    private String token;

    private Boolean current;

    private Long tokenTimeout;
}
