import type { RouteRecordRaw } from 'vue-router';
import { aliasField, idField, NORMAL_DISABLE_OPTIONS, textColumn, textField } from '../helpers';

export const userRoutes: RouteRecordRaw[] = [
  { path: 'dashboard', alias: ['/admin/dashboard'], name: 'AdminDashboard', component: () => import('@/pages/dashboard/admin-dashboard.vue') },
  { path: 'user', alias: ['/system/user', '/admin/system/user'], name: 'UserCenter', component: () => import('@/pages/user/user-list.vue') },
  { path: 'user/depts', alias: ['/system/dept', '/admin/system/dept'], name: 'DeptCenter', component: () => import('@/pages/user/dept-list.vue') },
  {
    path: 'user/roles',
    alias: ['/system/role', '/admin/system/role'],
    name: 'RoleCenter',
    component: () => import('@/pages/user/role-management.vue'),
    meta: {
      title: '角色权限',
      description: '维护角色、数据范围和角色状态。',
      endpoint: '/myow/system/role/page',
      baseEndpoint: '/myow/system/role',
      idKey: 'roleId',
      createPerm: 'system:role:add',
      updatePerm: 'system:role:update',
      deletePerm: 'system:role:delete',
      statusOptions: NORMAL_DISABLE_OPTIONS,
      columns: [
        textColumn('roleId', '角色 ID'),
        textColumn('roleCode', '角色编码', true),
        textColumn('roleName', '角色名称'),
        textColumn('dataScope', '数据范围'),
        textColumn('status', '状态'),
        textColumn('createTime', '创建时间')
      ],
      formFields: [
        idField('roleId', '角色 ID'),
        textField('roleCode', '角色编码'),
        textField('roleName', '角色名称'),
        textField('sort', '排序', 'number'),
        textField('dataScope', '数据范围'),
        textField('status', '状态', 'select', NORMAL_DISABLE_OPTIONS),
        textField('remark', '备注', 'textarea')
      ],
      requiredFields: ['roleCode', 'roleName', 'sort', 'dataScope', 'status']
    }
  },
  {
    path: 'user/menus',
    alias: ['/system/menu', '/admin/system/menu'],
    name: 'MenuCenter',
    component: () => import('@/pages/user/menu-management.vue'),
    meta: {
      title: '菜单权限',
      description: '维护后台菜单、按钮权限码和路由配置。',
      endpoint: '/myow/system/menu/page',
      baseEndpoint: '/myow/system/menu',
      idKey: 'menuId',
      createPerm: 'system:menu:add',
      updatePerm: 'system:menu:update',
      deletePerm: 'system:menu:delete',
      statusOptions: NORMAL_DISABLE_OPTIONS,
      columns: [
        textColumn('menuId', '菜单 ID'),
        textColumn('menuName', '菜单名称'),
        textColumn('menuType', '类型'),
        textColumn('path', '路由', true),
        textColumn('apiPerms', '权限码', true),
        textColumn('status', '状态')
      ],
      formFields: [
        idField('menuId', '菜单 ID'),
        textField('parentId', '上级菜单 ID'),
        textField('menuName', '菜单名称'),
        textField('menuType', '菜单类型'),
        textField('path', '路由地址'),
        textField('component', '组件路径'),
        textField('queryParam', '路由参数'),
        textField('isFrame', '是否外链'),
        textField('isCache', '是否缓存'),
        textField('visible', '显示状态'),
        textField('perms', '权限码'),
        textField('icon', '图标'),
        textField('sort', '排序', 'number'),
        textField('status', '状态', 'select', NORMAL_DISABLE_OPTIONS),
        textField('remark', '备注', 'textarea')
      ],
      requiredFields: ['parentId', 'menuName', 'sort', 'menuType', 'visible', 'status']
    }
  },
  {
    path: 'user/dicts',
    alias: ['/system/dict', '/admin/system/dict'],
    name: 'DictCenter',
    component: () => import('@/pages/user/dict-management.vue'),
    meta: {
      title: '字典管理',
      description: '维护系统字典类型和字典值。',
      endpoint: '/myow/system/dict/page',
      baseEndpoint: '/myow/system/dict',
      idKey: 'dictId',
      createPerm: 'system:dict:add',
      updatePerm: 'system:dict:update',
      deletePerm: 'system:dict:delete',
      statusOptions: NORMAL_DISABLE_OPTIONS,
      columns: [
        textColumn('dictId', '字典 ID'),
        textColumn('dictCode', '字典编码', true),
        textColumn('dictName', '字典名称'),
        textColumn('status', '状态'),
        textColumn('remark', '备注'),
        textColumn('createTime', '创建时间')
      ],
      formFields: [
        idField('dictId', '字典 ID'),
        textField('dictCode', '字典编码'),
        textField('dictName', '字典名称'),
        textField('remark', '备注', 'textarea')
      ],
      requiredFields: ['dictCode', 'dictName']
    }
  },
  {
    path: 'user/login-logs',
    alias: ['/system/login-log', '/admin/system/login-log'],
    name: 'LoginLogCenter',
    component: () => import('@/pages/common/generic-crud-list.vue'),
    meta: {
      title: '登录日志',
      description: '查看账号登录、失败原因和登录 IP。',
      endpoint: '/myow/system/login-log/page',
      baseEndpoint: '/myow/system/login-log',
      canCreate: false,
      canUpdate: false,
      canDelete: false,
      statusOptions: NORMAL_DISABLE_OPTIONS,
      columns: [
        textColumn('loginLogId', '日志 ID'),
        textColumn('loginName', '登录账号'),
        textColumn('ip', 'IP'),
        textColumn('status', '状态'),
        textColumn('failReason', '失败原因'),
        textColumn('createTime', '登录时间')
      ]
    }
  }
];
