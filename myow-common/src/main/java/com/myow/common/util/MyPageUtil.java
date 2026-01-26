package com.myow.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.PageParam;
import com.myow.common.response.PageResult;
import com.myow.common.response.ResultCode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author: yss
 * @date: 2026-01-20 22:03
 * @description: 分页工具类
 */
@Slf4j
public class MyPageUtil {

    /**
     * 转换为查询参数
     */
    public static <T> Page<T> convert2PageQuery(PageParam pageParam,  Class<T> tClass) {
        Page<T> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());

        List<PageParam.SortItem> sortItemList = pageParam.getSortItemList();
        if (CollUtil.isEmpty(sortItemList)) {
            return page;
        }

        // 设置排序字段并检测是否含有sql注入
        List<OrderItem> orderItemList = new ArrayList<>();
        for (PageParam.SortItem sortItem : sortItemList) {

            if (StrUtil.isEmpty(sortItem.getColumn())) {
                continue;
            }

            if (SqlInjectionUtils.check(sortItem.getColumn())) {
                log.error("《存在SQL注入：》 : {}", sortItem.getColumn());
                throw new BusinessException(ResultCode.SQL_INJECTION_ERROR);
            }
            orderItemList.add(sortItem.getAsc() ? OrderItem.asc(sortItem.getColumn()) : OrderItem.desc(sortItem.getColumn()));
        }

        page.setOrders(orderItemList);
        return page;
    }

    /**
     * 转换为 PageResult 对象
     */
    public static <T, E> PageResult<T> of(Page<E> page, Function<E, T> function) {
        List<T> sourceList = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList()).stream().map(function).toList();
        return convert2PageResult(page, sourceList);
    }

    /**
     * 转换为 PageResult 对象
     */
    private static <T, E> PageResult<T> convert2PageResult(Page<E> page, List<T> list) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setPageNum(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        pageResult.setPages(page.getPages());
        pageResult.setList(list);
        pageResult.setEmptyFlag(CollUtil.isEmpty(list));
        return pageResult;
    }

}
