-- Complete remaining user-center management menus for local/dev bootstrap.

INSERT INTO sys_menu(menu_id, menu_name, parent_id, sort, path, component, is_frame, is_cache, menu_type, visible, status, api_perms, icon, create_time, update_time, deleted_flag)
VALUES
    (1800, 'Tenant', 1000, 8, '/system/tenant', 'system/tenant/index', '1', '0', 'C', '0', '0', 'system:tenant:query', 'building', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1801, 'Tenant Register', 1800, 1, null, null, '1', '0', 'F', '0', '0', 'system:tenant:add', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1802, 'Tenant Status', 1800, 2, null, null, '1', '0', 'F', '0', '0', 'system:tenant:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1810, 'Tenant Plan', 1000, 9, '/system/tenant-plans', 'system/tenant-plans/index', '1', '0', 'C', '0', '0', 'system:tenant-plans:query', 'package', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1811, 'Tenant Plan Add', 1810, 1, null, null, '1', '0', 'F', '0', '0', 'system:tenant-plans:add', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1812, 'Tenant Plan Update', 1810, 2, null, null, '1', '0', 'F', '0', '0', 'system:tenant-plans:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1813, 'Tenant Plan Delete', 1810, 3, null, null, '1', '0', 'F', '0', '0', 'system:tenant-plans:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1820, 'Login Log', 1000, 10, '/system/login-log', 'system/login-log/index', '1', '0', 'C', '0', '0', 'system:login-log:query', 'log-in', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1830, 'Operation Log', 1000, 11, '/system/oper-log', 'system/oper-log/index', '1', '0', 'C', '0', '0', 'system:oper-log:query', 'logs', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1831, 'Operation Log Add', 1830, 1, null, null, '1', '0', 'F', '0', '0', 'system:oper-log:add', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1832, 'Operation Log Update', 1830, 2, null, null, '1', '0', 'F', '0', '0', 'system:oper-log:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1833, 'Operation Log Delete', 1830, 3, null, null, '1', '0', 'F', '0', '0', 'system:oper-log:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1840, 'I18n Key', 1000, 12, '/system/i18n-key', 'system/i18n-key/index', '1', '0', 'C', '0', '0', 'system:i18n-key:query', 'languages', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1841, 'I18n Key Add', 1840, 1, null, null, '1', '0', 'F', '0', '0', 'system:i18n-key:add', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1842, 'I18n Key Update', 1840, 2, null, null, '1', '0', 'F', '0', '0', 'system:i18n-key:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1843, 'I18n Key Delete', 1840, 3, null, null, '1', '0', 'F', '0', '0', 'system:i18n-key:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1850, 'I18n Message', 1000, 13, '/system/i18n-message', 'system/i18n-message/index', '1', '0', 'C', '0', '0', 'system:i18n-message:query', 'message-square-text', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1851, 'I18n Message Add', 1850, 1, null, null, '1', '0', 'F', '0', '0', 'system:i18n-message:add', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1852, 'I18n Message Update', 1850, 2, null, null, '1', '0', 'F', '0', '0', 'system:i18n-message:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1853, 'I18n Message Delete', 1850, 3, null, null, '1', '0', 'F', '0', '0', 'system:i18n-message:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1860, 'Serial No Config', 1000, 14, '/system/serial-no-config', 'system/serial-no-config/index', '1', '0', 'C', '0', '0', 'system:serial-no-config:query', 'hash', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1861, 'Serial No Config Add', 1860, 1, null, null, '1', '0', 'F', '0', '0', 'system:serial-no-config:add', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1862, 'Serial No Config Update', 1860, 2, null, null, '1', '0', 'F', '0', '0', 'system:serial-no-config:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1863, 'Serial No Config Delete', 1860, 3, null, null, '1', '0', 'F', '0', '0', 'system:serial-no-config:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1870, 'Serial No Record', 1000, 15, '/system/serial-no-record', 'system/serial-no-record/index', '1', '0', 'C', '0', '0', 'system:serial-no-record:query', 'list-ordered', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false)
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
WHERE menu_id BETWEEN 1800 AND 1870
ON CONFLICT (role_id, menu_id) DO NOTHING;
