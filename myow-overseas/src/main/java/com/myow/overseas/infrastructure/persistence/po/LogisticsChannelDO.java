package com.myow.overseas.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@TableName("owh_logistics_channel")
public class LogisticsChannelDO {
    @TableId(value = "channel_id", type = IdType.ASSIGN_ID)
    private Long channelId;
    private Long tenantId;
    private String channelCode;
    private String channelName;
    private Long carrierCustomerId;
    private String channelType;
    private String labelSource;
    private Long tmsSystemId;
    private String labelFormat;
    private String status;
    private String remark;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Boolean deletedFlag;
}
