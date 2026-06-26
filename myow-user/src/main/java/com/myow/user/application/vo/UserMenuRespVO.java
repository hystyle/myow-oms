package com.myow.user.application.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMenuRespVO {

    private Long menuId;

    private String menuName;

    private Long parentId;

    private Integer sort;

    private String path;

    private String component;

    private String queryParam;

    private String isFrame;

    private String isCache;

    private String menuType;

    private String visible;

    private String status;

    private String apiPerms;

    private String icon;

    private String remark;
}
