-- Complete system support button permissions added after the original system seed.

INSERT INTO sys_menu(menu_id, menu_name, parent_id, sort, path, component, is_frame, is_cache, menu_type, visible, status, api_perms, icon, create_time, update_time, deleted_flag)
VALUES
    (1933, 'File Download', 1930, 3, null, null, '1', '0', 'F', '0', '0', 'system:file:download', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1964, 'Message Template Preview', 1960, 4, null, null, '1', '0', 'F', '0', '0', 'system:message-template:preview', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1973, 'Export Task Download', 1970, 3, null, null, '1', '0', 'F', '0', '0', 'system:export-task:download', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false)
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
WHERE menu_id IN (1933, 1964, 1973)
ON CONFLICT (role_id, menu_id) DO NOTHING;
