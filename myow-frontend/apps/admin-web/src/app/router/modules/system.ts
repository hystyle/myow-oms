import type { RouteRecordRaw } from 'vue-router';
import { aliasField, idField, SYSTEM_STATUS_OPTIONS, textColumn, textField } from '../helpers';

export const systemRoutes: RouteRecordRaw[] = [
  { path: 'system', name: 'SystemCenter', component: () => import('@/pages/system/system-overview.vue') },
  {
    path: 'system/jobs',
    alias: ['/system-support/jobs'],
    name: 'JobCrudCenter',
    component: () => import('@/pages/common/generic-crud-list.vue'),
    meta: {
      title: '定时任务',
      description: '维护系统调度任务、Cron 表达式和处理器。',
      endpoint: '/myow/api/v1/system/jobs/page',
      baseEndpoint: '/myow/api/v1/system/jobs',
      createPerm: 'system:job:create',
      updatePerm: 'system:job:update',
      deletePerm: 'system:job:delete',
      statusOptions: SYSTEM_STATUS_OPTIONS,
      columns: [
        textColumn('id', '任务 ID'),
        textColumn('jobName', '任务名称', false, ['code']),
        textColumn('jobGroup', '任务组', true, ['name']),
        textColumn('cronExpression', 'Cron', true),
        textColumn('handlerName', '处理器', true),
        textColumn('status', '状态')
      ],
      formFields: [
        idField('id', '任务 ID'),
        aliasField('jobName', '任务名称', ['code']),
        aliasField('jobGroup', '任务组', ['name']),
        textField('cronExpression', 'Cron 表达式'),
        textField('handlerName', '处理器 Bean'),
        textField('status', '状态', 'select', SYSTEM_STATUS_OPTIONS)
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
    component: () => import('@/pages/common/generic-crud-list.vue'),
    meta: {
      title: '通知公告',
      description: '维护系统公告草稿、内容、类型和有效期。',
      endpoint: '/myow/api/v1/system/notices/page',
      baseEndpoint: '/myow/api/v1/system/notices',
      createPerm: 'system:notice:create',
      updatePerm: 'system:notice:update',
      deletePerm: 'system:notice:delete',
      statusOptions: SYSTEM_STATUS_OPTIONS,
      columns: [
        textColumn('id', '公告 ID'),
        textColumn('title', '标题', false, ['code']),
        textColumn('noticeType', '类型'),
        textColumn('status', '状态'),
        textColumn('expireTime', '有效期'),
        textColumn('updateTime', '更新时间')
      ],
      formFields: [
        idField('id', '公告 ID'),
        aliasField('title', '标题', ['code']),
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
    component: () => import('@/pages/common/generic-crud-list.vue'),
    meta: {
      title: '站点配置',
      description: '维护站点级参数、开关和敏感配置。',
      endpoint: '/myow/api/v1/system/site-configs/page',
      baseEndpoint: '/myow/api/v1/system/site-configs',
      createPerm: 'system:site-config:create',
      updatePerm: 'system:site-config:update',
      deletePerm: 'system:site-config:delete',
      statusOptions: SYSTEM_STATUS_OPTIONS,
      columns: [
        textColumn('id', 'ID'),
        textColumn('siteCode', '站点', true, ['name']),
        textColumn('configKey', '配置键', true, ['code']),
        textColumn('configValue', '配置值'),
        textColumn('status', '状态'),
        textColumn('updateTime', '更新时间')
      ],
      formFields: [
        idField('id', '配置 ID'),
        aliasField('siteCode', '站点编码', ['name']),
        aliasField('configKey', '配置键', ['code']),
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
    component: () => import('@/pages/common/generic-crud-list.vue'),
    meta: {
      title: '文件管理',
      description: '查看上传文件、模块归属、大小和删除策略。',
      endpoint: '/myow/api/v1/system/files/page',
      baseEndpoint: '/myow/api/v1/system/files',
      deletePerm: 'system:file:delete',
      canCreate: false,
      canUpdate: false,
      statusOptions: SYSTEM_STATUS_OPTIONS,
      columns: [
        textColumn('id', '文件 ID'),
        textColumn('fileName', '文件名', false, ['code']),
        textColumn('moduleCode', '模块', true, ['name']),
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
    component: () => import('@/pages/common/generic-crud-list.vue'),
    meta: {
      title: '在线用户',
      description: '查看当前在线会话与踢出控制入口。',
      endpoint: '/myow/api/v1/system/online-users/page',
      baseEndpoint: '/myow/api/v1/system/online-users',
      canCreate: false,
      canUpdate: false,
      canDelete: false,
      statusOptions: SYSTEM_STATUS_OPTIONS,
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
    component: () => import('@/pages/common/generic-crud-list.vue'),
    meta: {
      title: '敏感词',
      description: '维护敏感词库和文本检查基础数据。',
      endpoint: '/myow/api/v1/system/sensitive-words/page',
      baseEndpoint: '/myow/api/v1/system/sensitive-words',
      createPerm: 'system:sensitive-word:create',
      updatePerm: 'system:sensitive-word:update',
      deletePerm: 'system:sensitive-word:delete',
      statusOptions: SYSTEM_STATUS_OPTIONS,
      columns: [
        textColumn('id', 'ID'),
        textColumn('word', '敏感词', false, ['code']),
        textColumn('category', '分类', false, ['name']),
        textColumn('level', '等级'),
        textColumn('status', '状态'),
        textColumn('updateTime', '更新时间')
      ],
      formFields: [
        idField('id', '敏感词 ID'),
        aliasField('word', '敏感词', ['code']),
        aliasField('category', '分类', ['name']),
        textField('level', '等级', 'number'),
        textField('replacement', '替换文本'),
        textField('status', '状态', 'select', SYSTEM_STATUS_OPTIONS)
      ],
      requiredFields: ['word', 'category', 'level']
    }
  },
  {
    path: 'system/message-templates',
    alias: ['/system-support/message-templates'],
    name: 'MessageTemplateCenter',
    component: () => import('@/pages/common/generic-crud-list.vue'),
    meta: {
      title: '消息模板',
      description: '维护通知、邮件、站内信等消息模板。',
      endpoint: '/myow/api/v1/system/message-templates/page',
      baseEndpoint: '/myow/api/v1/system/message-templates',
      createPerm: 'system:message-template:create',
      updatePerm: 'system:message-template:update',
      deletePerm: 'system:message-template:delete',
      statusOptions: SYSTEM_STATUS_OPTIONS,
      columns: [
        textColumn('id', '模板 ID'),
        textColumn('templateCode', '模板编码', true, ['code']),
        textColumn('templateName', '模板名称', false, ['name']),
        textColumn('channel', '渠道'),
        textColumn('status', '状态'),
        textColumn('updateTime', '更新时间')
      ],
      formFields: [
        idField('id', '模板 ID'),
        aliasField('templateCode', '模板编码', ['code']),
        textField('channel', '消息渠道'),
        aliasField('title', '标题', ['name']),
        textField('content', '内容', 'textarea'),
        textField('variables', '变量定义', 'textarea'),
        textField('status', '状态', 'select', SYSTEM_STATUS_OPTIONS)
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
    component: () => import('@/pages/common/generic-crud-list.vue'),
    meta: {
      title: '导出任务',
      description: '查看我的导出任务、执行状态和下载结果。',
      endpoint: '/myow/api/v1/system/export-tasks/my-page',
      baseEndpoint: '/myow/api/v1/system/export-tasks',
      createPerm: 'system:export-task:create',
      deletePerm: 'system:export-task:delete',
      canUpdate: false,
      statusOptions: SYSTEM_STATUS_OPTIONS,
      columns: [
        textColumn('id', '任务 ID'),
        textColumn('moduleName', '模块名称', true, ['code']),
        textColumn('exportType', '导出类型', false, ['name']),
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
  { path: 'system/monitor', alias: ['/system-support/monitor'], name: 'MonitorCenter', component: () => import('@/pages/system/system-monitor.vue') }
];
