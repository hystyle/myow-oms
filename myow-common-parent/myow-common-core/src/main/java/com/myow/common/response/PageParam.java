package com.myow.common.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * @author: yss
 * @date: 2026-01-20 22:07
 * @description: 分页基础参数
 */
@Data
public class PageParam {
    @NotNull(message = "分页参数不能为空")
    private Long pageNum;
    @NotNull(message = "每页数量不能为空")
    @Max(value = 500, message = "每页最大为500")
    private Long pageSize;
    @Size(max = 10, message = "排序字段最多10")
    @Valid
    private List<SortItem> sortItemList;

    /**
     * 排序DTO类
     */
    @Data
    public static class SortItem {
        @NotNull(message = "排序规则不能为空")
        private Boolean asc;
        @NotBlank(message = "排序字段不能为空")
        @Length(max = 30, message = "排序字段最多30")
        private String column;
    }
}
