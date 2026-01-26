package com.myow.system.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yss
 * @since 2026-01-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_i18n_key")
public class I18nKeyDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String keyCode;

    private String bizDomain;

    private String description;

    private Short status;

    private String createdBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
