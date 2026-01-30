package com.myow.system.domain.consts;

/**
 * @author: yss
 * @date: 2026-01-27 23:00
 * @description: 系统缓存常量
 */
public class SystemCacheConst {

    public static class Login {
        /**
         * 登录用户缓存
         */
        public static final String LOGIN_USER = "login_user";

        /**
         * 用户权限缓存
         */
        public static final String USER_PERMISSION = "user_permission";
    }

    public static class Department {

        /**
         * 部门列表
         */
        public static final String DEPARTMENT_LIST = "department_list";

        /**
         * 部门树
         */
        public static final String DEPARTMENT_TREE = "department_tree";

        /**
         * 某个部门以及下级的id列表
         */
        public static final String DEPARTMENT_SELF_CHILDREN = "department_self_children";

        /**
         * 部门路径 缓存
         */
        public static final String DEPARTMENT_PATH = "department_path";

    }

}
