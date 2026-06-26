package com.myow.user.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_menu")
public class MenuDO {

    @TableId("menu_id")
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

    private String apiPerms;

    private String icon;

    private String status;

    private Boolean deletedFlag;

    private String remark;
}
