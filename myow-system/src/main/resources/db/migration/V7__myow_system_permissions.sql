-- System support menus and permissions for local/dev bootstrap.

INSERT INTO sys_menu(menu_id, menu_name, parent_id, sort, path, component, is_frame, is_cache, menu_type, visible, status, api_perms, icon, create_time, update_time, deleted_flag)
VALUES
    (1900, 'System Support', 1000, 16, '/system-support', 'Layout', '1', '0', 'M', '0', '0', null, 'settings-2', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),

    (1910, 'Scheduled Job', 1900, 1, '/system-support/jobs', 'system-support/job/index', '1', '0', 'C', '0', '0', 'system:job:list', 'timer', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1911, 'Scheduled Job Create', 1910, 1, null, null, '1', '0', 'F', '0', '0', 'system:job:create', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1912, 'Scheduled Job Update', 1910, 2, null, null, '1', '0', 'F', '0', '0', 'system:job:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1913, 'Scheduled Job Delete', 1910, 3, null, null, '1', '0', 'F', '0', '0', 'system:job:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1914, 'Scheduled Job Run', 1910, 4, null, null, '1', '0', 'F', '0', '0', 'system:job:run', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1915, 'Scheduled Job Pause', 1910, 5, null, null, '1', '0', 'F', '0', '0', 'system:job:pause', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1916, 'Scheduled Job Resume', 1910, 6, null, null, '1', '0', 'F', '0', '0', 'system:job:resume', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),

    (1920, 'Notice', 1900, 2, '/system-support/notices', 'system-support/notice/index', '1', '0', 'C', '0', '0', 'system:notice:list', 'megaphone', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1921, 'Notice Create', 1920, 1, null, null, '1', '0', 'F', '0', '0', 'system:notice:create', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1922, 'Notice Update', 1920, 2, null, null, '1', '0', 'F', '0', '0', 'system:notice:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1923, 'Notice Delete', 1920, 3, null, null, '1', '0', 'F', '0', '0', 'system:notice:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1924, 'Notice Publish', 1920, 4, null, null, '1', '0', 'F', '0', '0', 'system:notice:publish', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1925, 'Notice Withdraw', 1920, 5, null, null, '1', '0', 'F', '0', '0', 'system:notice:withdraw', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),

    (1930, 'File', 1900, 3, '/system-support/files', 'system-support/file/index', '1', '0', 'C', '0', '0', 'system:file:list', 'file', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1931, 'File Upload', 1930, 1, null, null, '1', '0', 'F', '0', '0', 'system:file:upload', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1932, 'File Delete', 1930, 2, null, null, '1', '0', 'F', '0', '0', 'system:file:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),

    (1940, 'Site Config', 1900, 4, '/system-support/site-configs', 'system-support/site-config/index', '1', '0', 'C', '0', '0', 'system:site-config:list', 'sliders', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1941, 'Site Config Create', 1940, 1, null, null, '1', '0', 'F', '0', '0', 'system:site-config:create', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1942, 'Site Config Update', 1940, 2, null, null, '1', '0', 'F', '0', '0', 'system:site-config:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1943, 'Site Config Delete', 1940, 3, null, null, '1', '0', 'F', '0', '0', 'system:site-config:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1944, 'Site Config Refresh', 1940, 4, null, null, '1', '0', 'F', '0', '0', 'system:site-config:refresh', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),

    (1950, 'Sensitive Word', 1900, 5, '/system-support/sensitive-words', 'system-support/sensitive-word/index', '1', '0', 'C', '0', '0', 'system:sensitive-word:list', 'shield-alert', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1951, 'Sensitive Word Create', 1950, 1, null, null, '1', '0', 'F', '0', '0', 'system:sensitive-word:create', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1952, 'Sensitive Word Update', 1950, 2, null, null, '1', '0', 'F', '0', '0', 'system:sensitive-word:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1953, 'Sensitive Word Delete', 1950, 3, null, null, '1', '0', 'F', '0', '0', 'system:sensitive-word:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1954, 'Sensitive Word Import', 1950, 4, null, null, '1', '0', 'F', '0', '0', 'system:sensitive-word:import', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),

    (1960, 'Message Template', 1900, 6, '/system-support/message-templates', 'system-support/message-template/index', '1', '0', 'C', '0', '0', 'system:message-template:list', 'mail', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1961, 'Message Template Create', 1960, 1, null, null, '1', '0', 'F', '0', '0', 'system:message-template:create', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1962, 'Message Template Update', 1960, 2, null, null, '1', '0', 'F', '0', '0', 'system:message-template:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1963, 'Message Template Delete', 1960, 3, null, null, '1', '0', 'F', '0', '0', 'system:message-template:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),

    (1970, 'Export Task', 1900, 7, '/system-support/export-tasks', 'system-support/export-task/index', '1', '0', 'C', '0', '0', 'system:export-task:list', 'download', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1971, 'Export Task Create', 1970, 1, null, null, '1', '0', 'F', '0', '0', 'system:export-task:create', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1972, 'Export Task Delete', 1970, 2, null, null, '1', '0', 'F', '0', '0', 'system:export-task:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),

    (1980, 'Monitor', 1900, 8, '/system-support/monitor', 'system-support/monitor/index', '1', '0', 'C', '0', '0', 'system:monitor:view', 'activity', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1990, 'Online User', 1900, 9, '/system-support/online-users', 'system-support/online-user/index', '1', '0', 'C', '0', '0', 'system:online-user:list', 'users', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1991, 'Online User Kick', 1990, 1, null, null, '1', '0', 'F', '0', '0', 'system:online-user:kick', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false)
ON CONFLICT (menu_id) DO UPDATE SET
    menu_name = EXCLUDED.menu_name,
    parent_id = EXCLUDED.parent_id,
    sort = EXCLUDED.sort,
    path = EXCLUDED.path,
    component = EXCLUDED.component,
    menu_type = EXCLUDED.menu_type,
    visible = EXCLUDED.visible,
    status = EXCLUDED.status,
    api_perms = EXCLUDED.api_perms,
    icon = EXCLUDED.icon,
    deleted_flag = false,
    update_time = CURRENT_TIMESTAMP(3);

INSERT INTO sys_role_menu(role_id, menu_id)
SELECT 1, menu_id
FROM sys_menu
WHERE menu_id BETWEEN 1900 AND 1991
ON CONFLICT (role_id, menu_id) DO NOTHING;
