package com.myow.common.response;

import lombok.Data;

import java.util.List;

/**
 * @author: yss
 * @date: 2026-01-20 21:57
 * @description: 分页返回对象
 */
@Data
public class PageResult<T> {

    /**
     * 当前页
     */
    private Long pageNum;

    /**
     * 每页的数量
     */
    private Long pageSize;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 结果集
     */
    private List<T> list;

    /**
     * 是否为空
     */
    private Boolean emptyFlag;

    /**
     * 创建一个空的结果集
     */
    public static <T> PageResult<T> empty() {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setEmptyFlag(true);
        return pageResult;
    }

}
