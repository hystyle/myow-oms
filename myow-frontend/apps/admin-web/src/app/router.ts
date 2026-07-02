import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

const textColumn = (key: string, label: string, code = false) => ({ key, label, code });
const textField = (
  key: string,
  label: string,
  type: 'text' | 'number' | 'textarea' | 'select' | 'datetime' = 'text',
  options?: Array<{ label: string; value: string | number }>,
  extra: Record<string, unknown> = {}
) => ({ key, label, type, options, ...extra });
const idField = (key: string, label: string = 'ID') => textField(key, label, 'number', undefined, { hideOnCreate: true, readonly: true });
const normalDisableOptions = [{ label: '正常', value: '0' }, { label: '停用', value: '1' }];
const systemStatusOptions = [{ label: '启用', value: 1 }, { label: '停用', value: 0 }];

export const router = createRouter({
  history: createWebHistory('/'),
  routes: [
    { path: '/login', name: 'AdminLogin', component: () => import('@/pages/login/AdminLogin.vue'), meta: { public: true } },
    {
      path: '/',
      component: () => import('@/layouts/AdminLayout.vue'),
      children: [
        { path: '', redirect: '/dashboard' },
        { path: 'dashboard', name: 'AdminDashboard', component: () => import('@/pages/dashboard/AdminDashboard.vue') },
        { path: 'user', alias: ['/system/user'], name: 'UserCenter', component: () => import('@/pages/user/UserCenter.vue') },
        { path: 'user/depts', alias: ['/system/dept'], name: 'DeptCenter', component: () => import('@/pages/user/DeptCenter.vue') },
        {
          path: 'user/roles',
          alias: ['/system/role'],
          name: 'RoleCenter',
          component: () => import('@/pages/common/AdminGenericPage.vue'),
          meta: {
            title: '角色权限',
            description: '维护角色、数据范围和角色状态。',
            endpoint: '/myow/system/role/page',
            baseEndpoint: '/myow/system/role',
            idKey: 'roleId',
            createPerm: 'system:role:add',
            updatePerm: 'system:role:update',
            deletePerm: 'system:role:delete',
            statusOptions: normalDisableOptions,
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
              textField('status', '状态', 'select', normalDisableOptions),
              textField('remark', '备注', 'textarea')
            ],
            requiredFields: ['roleCode', 'roleName', 'sort', 'dataScope', 'status']
          }
        },
        {
          path: 'user/menus',
          alias: ['/system/menu'],
          name: 'MenuCenter',
          component: () => import('@/pages/common/AdminGenericPage.vue'),
          meta: {
            title: '菜单权限',
            description: '维护后台菜单、按钮权限码和路由配置。',
            endpoint: '/myow/system/menu/page',
            baseEndpoint: '/myow/system/menu',
            idKey: 'menuId',
            createPerm: 'system:menu:add',
            updatePerm: 'system:menu:update',
            deletePerm: 'system:menu:delete',
            statusOptions: normalDisableOptions,
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
              textField('parentId', '上级菜单 ID', 'number'),
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
              textField('status', '状态', 'select', normalDisableOptions),
              textField('remark', '备注', 'textarea')
            ],
            requiredFields: ['parentId', 'menuName', 'sort', 'menuType', 'visible', 'status']
          }
        },
        {
          path: 'user/dicts',
          alias: ['/system/dict'],
          name: 'DictCenter',
          component: () => import('@/pages/common/AdminGenericPage.vue'),
          meta: {
            title: '字典管理',
            description: '维护系统字典类型和字典值。',
            endpoint: '/myow/system/dict/page',
            baseEndpoint: '/myow/system/dict',
            idKey: 'dictId',
            createPerm: 'system:dict:add',
            updatePerm: 'system:dict:update',
            deletePerm: 'system:dict:delete',
            statusOptions: normalDisableOptions,
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
          alias: ['/system/login-log'],
          name: 'LoginLogCenter',
          component: () => import('@/pages/common/AdminGenericPage.vue'),
          meta: {
            title: '登录日志',
            description: '查看账号登录、失败原因和登录 IP。',
            endpoint: '/myow/system/login-log/page',
            baseEndpoint: '/myow/system/login-log',
            canCreate: false,
            canUpdate: false,
            canDelete: false,
            statusOptions: normalDisableOptions,
            columns: [
              textColumn('loginLogId', '日志 ID'),
              textColumn('loginName', '登录账号'),
              textColumn('ip', 'IP'),
              textColumn('status', '状态'),
              textColumn('failReason', '失败原因'),
              textColumn('createTime', '登录时间')
            ]
          }
        },
        { path: 'system', name: 'SystemCenter', component: () => import('@/pages/system/SystemCenter.vue') },
        {
          path: 'system/jobs',
          alias: ['/system-support/jobs'],
          name: 'JobCrudCenter',
          component: () => import('@/pages/common/AdminGenericPage.vue'),
          meta: {
            title: '定时任务',
            description: '维护系统调度任务、Cron 表达式和处理器。',
            endpoint: '/myow/api/v1/system/jobs/page',
            baseEndpoint: '/myow/api/v1/system/jobs',
            createPerm: 'system:job:create',
            updatePerm: 'system:job:update',
            deletePerm: 'system:job:delete',
            statusOptions: systemStatusOptions,
            columns: [
              textColumn('id', '任务 ID'),
              textColumn('jobName', '任务名称'),
              textColumn('jobGroup', '任务组', true),
              textColumn('cronExpression', 'Cron', true),
              textColumn('handlerName', '处理器', true),
              textColumn('status', '状态')
            ],
            formFields: [
              idField('id', '任务 ID'),
              textField('jobName', '任务名称'),
              textField('jobGroup', '任务组'),
              textField('cronExpression', 'Cron 表达式'),
              textField('handlerName', '处理器 Bean'),
              textField('status', '状态', 'select', systemStatusOptions)
            ],
            requiredFields: ['jobName', 'jobGroup', 'cronExpression', 'handlerName'],
            rowActions: [
              { label: '执行一次', endpoint: '/myow/api/v1/system/jobs/run', permission: 'system:job:run', confirm: '确认立即执行该任务？', success: '任务已提交执行' },
              { label: '暂停', endpoint: '/myow/api/v1/system/jobs/pause', permission: 'system:job:pause', confirm: '确认暂停该任务？', success: '任务已暂停' },
              { label: '恢复', endpoint: '/myow/api/v1/system/jobs/resume', permission: 'system:job:resume', confirm: '确认恢复该任务？', success: '任务已恢复' }
            ]
          }
        },
        {
          path: 'system/notices',
          alias: ['/system-support/notices'],
          name: 'NoticeCrudCenter',
          component: () => import('@/pages/common/AdminGenericPage.vue'),
          meta: {
            title: '通知公告',
            description: '维护系统公告草稿、内容、类型和有效期。',
            endpoint: '/myow/api/v1/system/notices/page',
            baseEndpoint: '/myow/api/v1/system/notices',
            createPerm: 'system:notice:create',
            updatePerm: 'system:notice:update',
            deletePerm: 'system:notice:delete',
            statusOptions: systemStatusOptions,
            columns: [
              textColumn('id', '公告 ID'),
              textColumn('title', '标题'),
              textColumn('noticeType', '类型'),
              textColumn('status', '状态'),
              textColumn('expireTime', '有效期'),
              textColumn('updateTime', '更新时间')
            ],
            formFields: [
              idField('id', '公告 ID'),
              textField('title', '标题'),
              textField('noticeType', '公告类型'),
              textField('expireTime', '有效期', 'datetime'),
              textField('content', '公告内容', 'textarea')
            ],
            requiredFields: ['title', 'noticeType', 'content'],
            rowActions: [
              { label: '发布', endpoint: '/myow/api/v1/system/notices/publish', permission: 'system:notice:publish', confirm: '确认发布该公告？', success: '公告已发布' },
              { label: '下线', endpoint: '/myow/api/v1/system/notices/withdraw', permission: 'system:notice:withdraw', confirm: '确认下线该公告？', success: '公告已下线' }
            ]
          }
        },
        {
          path: 'system/site-configs',
          alias: ['/system-support/site-configs'],
          name: 'SiteConfigCenter',
          component: () => import('@/pages/common/AdminGenericPage.vue'),
          meta: {
            title: '站点配置',
            description: '维护站点级参数、开关和敏感配置。',
            endpoint: '/myow/api/v1/system/site-configs/page',
            baseEndpoint: '/myow/api/v1/system/site-configs',
            createPerm: 'system:site-config:create',
            updatePerm: 'system:site-config:update',
            deletePerm: 'system:site-config:delete',
            statusOptions: systemStatusOptions,
            columns: [
              textColumn('id', 'ID'),
              textColumn('siteCode', '站点', true),
              textColumn('configKey', '配置键', true),
              textColumn('configValue', '配置值'),
              textColumn('status', '状态'),
              textColumn('updateTime', '更新时间')
            ],
            formFields: [
              idField('id', '配置 ID'),
              textField('siteCode', '站点编码'),
              textField('configKey', '配置键'),
              textField('configValue', '配置值', 'textarea'),
              textField('configType', '配置类型'),
              textField('remark', '备注', 'textarea')
            ],
            requiredFields: ['siteCode', 'configKey', 'configValue', 'configType'],
            rowActions: [
              { label: '刷新缓存', endpoint: '/myow/api/v1/system/site-configs/refresh', permission: 'system:site-config:refresh', payloadKey: 'siteCode', idKey: 'siteCode', success: '配置缓存已刷新', refresh: false }
            ]
          }
        },
        {
          path: 'system/files',
          alias: ['/system-support/files'],
          name: 'FileCenter',
          component: () => import('@/pages/common/AdminGenericPage.vue'),
          meta: {
            title: '文件管理',
            description: '查看上传文件、模块归属、大小和删除策略。',
            endpoint: '/myow/api/v1/system/files/page',
            baseEndpoint: '/myow/api/v1/system/files',
            deletePerm: 'system:file:delete',
            canCreate: false,
            canUpdate: false,
            statusOptions: systemStatusOptions,
            columns: [
              textColumn('id', '文件 ID'),
              textColumn('fileName', '文件名'),
              textColumn('moduleCode', '模块', true),
              textColumn('fileSize', '大小'),
              textColumn('status', '状态'),
              textColumn('createTime', '上传时间')
            ],
            rowActions: [
              { label: '下载', endpoint: '/myow/api/v1/system/files/download', permission: 'system:file:download', resultMode: 'download', success: '文件下载已开始', refresh: false }
            ]
          }
        },
        {
          path: 'system/online-users',
          alias: ['/system-support/online-users'],
          name: 'OnlineUserCenter',
          component: () => import('@/pages/common/AdminGenericPage.vue'),
          meta: {
            title: '在线用户',
            description: '查看当前在线会话与踢出控制入口。',
            endpoint: '/myow/api/v1/system/online-users/page',
            baseEndpoint: '/myow/api/v1/system/online-users',
            canCreate: false,
            canUpdate: false,
            canDelete: false,
            statusOptions: systemStatusOptions,
            columns: [
              textColumn('userId', '用户 ID'),
              textColumn('loginName', '账号'),
              textColumn('nickName', '姓名'),
              textColumn('ip', 'IP'),
              textColumn('loginTime', '登录时间'),
              textColumn('expireTime', '过期时间')
            ],
            rowActions: [
              { label: '踢出', endpoint: '/myow/api/v1/system/online-users/kick', permission: 'system:online-user:kick', payloadKey: 'token', idKey: 'token', confirm: '确认踢出该在线用户？', success: '用户已踢出' }
            ]
          }
        },
        {
          path: 'system/sensitive-words',
          alias: ['/system-support/sensitive-words'],
          name: 'SensitiveWordCenter',
          component: () => import('@/pages/common/AdminGenericPage.vue'),
          meta: {
            title: '敏感词',
            description: '维护敏感词库和文本检查基础数据。',
            endpoint: '/myow/api/v1/system/sensitive-words/page',
            baseEndpoint: '/myow/api/v1/system/sensitive-words',
            createPerm: 'system:sensitive-word:create',
            updatePerm: 'system:sensitive-word:update',
            deletePerm: 'system:sensitive-word:delete',
            statusOptions: systemStatusOptions,
            columns: [
              textColumn('id', 'ID'),
              textColumn('word', '敏感词'),
              textColumn('wordType', '类型'),
              textColumn('status', '状态'),
              textColumn('remark', '备注'),
              textColumn('updateTime', '更新时间')
            ],
            formFields: [
              idField('id', '敏感词 ID'),
              textField('word', '敏感词'),
              textField('category', '分类'),
              textField('level', '等级', 'number'),
              textField('replacement', '替换文本'),
              textField('status', '状态', 'select', systemStatusOptions)
            ],
            requiredFields: ['word', 'category', 'level']
          }
        },
        {
          path: 'system/message-templates',
          alias: ['/system-support/message-templates'],
          name: 'MessageTemplateCenter',
          component: () => import('@/pages/common/AdminGenericPage.vue'),
          meta: {
            title: '消息模板',
            description: '维护通知、邮件、站内信等消息模板。',
            endpoint: '/myow/api/v1/system/message-templates/page',
            baseEndpoint: '/myow/api/v1/system/message-templates',
            createPerm: 'system:message-template:create',
            updatePerm: 'system:message-template:update',
            deletePerm: 'system:message-template:delete',
            statusOptions: systemStatusOptions,
            columns: [
              textColumn('id', '模板 ID'),
              textColumn('templateCode', '模板编码', true),
              textColumn('templateName', '模板名称'),
              textColumn('channelType', '渠道'),
              textColumn('status', '状态'),
              textColumn('updateTime', '更新时间')
            ],
            formFields: [
              idField('id', '模板 ID'),
              textField('templateCode', '模板编码'),
              textField('channel', '消息渠道'),
              textField('title', '标题'),
              textField('content', '内容', 'textarea'),
              textField('variables', '变量定义', 'textarea'),
              textField('status', '状态', 'select', systemStatusOptions)
            ],
            requiredFields: ['templateCode', 'channel', 'title', 'content'],
            rowActions: [
              { label: '预览', endpoint: '/myow/api/v1/system/message-templates/preview', permission: 'system:message-template:preview', resultMode: 'drawer', variablesPrompt: true, refresh: false }
            ]
          }
        },
        {
          path: 'system/export-tasks',
          alias: ['/system-support/export-tasks'],
          name: 'ExportTaskCenter',
          component: () => import('@/pages/common/AdminGenericPage.vue'),
          meta: {
            title: '导出任务',
            description: '查看我的导出任务、执行状态和下载结果。',
            endpoint: '/myow/api/v1/system/export-tasks/my-page',
            baseEndpoint: '/myow/api/v1/system/export-tasks',
            createPerm: 'system:export-task:create',
            deletePerm: 'system:export-task:delete',
            canUpdate: false,
            statusOptions: systemStatusOptions,
            columns: [
              textColumn('id', '任务 ID'),
              textColumn('taskName', '任务名称'),
              textColumn('moduleCode', '模块', true),
              textColumn('status', '状态'),
              textColumn('fileId', '文件 ID'),
              textColumn('createTime', '创建时间')
            ],
            formFields: [
              textField('moduleName', '模块名称'),
              textField('exportType', '导出类型'),
              textField('queryParams', '查询参数 JSON', 'textarea', undefined, { json: true })
            ],
            requiredFields: ['moduleName', 'exportType'],
            rowActions: [
              { label: '下载', endpoint: '/myow/api/v1/system/export-tasks/download', permission: 'system:export-task:download', resultMode: 'download', success: '导出文件下载已开始', refresh: false }
            ]
          }
        },
        { path: 'system/monitor', alias: ['/system-support/monitor'], name: 'MonitorCenter', component: () => import('@/pages/system/MonitorCenter.vue') }
      ]
    },
    { path: '/403', name: 'Forbidden', component: () => import('@/pages/error/Forbidden.vue'), meta: { public: true } },
    { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('@/pages/error/NotFound.vue'), meta: { public: true } }
  ]
});

router.beforeEach(async (to) => {
  if (to.meta.public) {
    return true;
  }
  const authStore = useAuthStore();
  if (!authStore.token) {
    return { name: 'AdminLogin', query: { redirect: to.fullPath } };
  }
  if (!authStore.bootstrapped) {
    await authStore.bootstrap();
  }
  return true;
});
