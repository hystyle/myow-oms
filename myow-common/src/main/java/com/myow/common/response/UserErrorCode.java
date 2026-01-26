package com.myow.common.response;

import lombok.Getter;

/**
 * @author: yss
 * @date: 2026-01-20 20:50
 * @description: 用户错误码
 */
@Getter
public enum UserErrorCode implements ErrorCode {

    DATA_NOT_EXIST(30001, "左翻右翻，数据竟然找不到了~"),

    ALREADY_EXIST(30002, "数据已存在了呀~"),

    REPEAT_SUBMIT(30003, "亲~您操作的太快了，请稍等下再操作~"),

    NO_PERMISSION(30004, "对不起，您没有权限访问此内容哦~"),

    DEVELOPING(30005, "系統正在紧急开发中，敬请期待~"),

    LOGIN_STATE_INVALID(30006, "您还未登录或登录失效，请重新登录！"),

    USER_NOT_EXIST(30007, "用户不存在"),

    USER_STATUS_ERROR(30008, "用户状态异常"),

    FORM_REPEAT_SUBMIT(30009, "请勿重复提交"),

    LOGIN_FAIL_LOCK(30010, "登录连续失败已经被锁定，无法登录"),

    LOGIN_FAIL_WILL_LOCK(30011, "登录连续失败将会锁定提醒"),

    LOGIN_ACTIVE_TIMEOUT(30012, "长时间未操作系统，需要重新登录"),

    EMAIL_ALREADY_EXIST(30013, "邮箱已被使用"),

    PHONE_ALREADY_EXIST(30014, "手机号已被使用"),

    DEPT_NOT_EXIST(30015, "部门不存在"),

    ROLE_NOT_EXIST(30016, "角色不存在"),

    MENU_NOT_EXIST(30017, "菜单不存在"),

    DICT_NOT_EXIST(30018, "字典不存在"),

    TENANT_NOT_EXIST(30019, "租户不存在"),

    POSITION_NOT_EXIST(30020, "岗位不存在");


    private final long code;
    private final String message;

    UserErrorCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

}
