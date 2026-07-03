-- Customer role permission seed.

INSERT INTO sys_menu(menu_id, menu_name, parent_id, sort, path, component, is_frame, is_cache, menu_type, visible, status, api_perms, icon, create_time, update_time, deleted_flag)
VALUES
    (4104, 'Customer Role List', 4100, 4, null, null, '1', '0', 'F', '0', '0', 'customer:role:list', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4105, 'Customer Role Create', 4100, 5, null, null, '1', '0', 'F', '0', '0', 'customer:role:create', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4106, 'Customer Role Update', 4100, 6, null, null, '1', '0', 'F', '0', '0', 'customer:role:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4107, 'Customer Role Delete', 4100, 7, null, null, '1', '0', 'F', '0', '0', 'customer:role:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false)
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
WHERE menu_id IN (4104, 4105, 4106, 4107)
ON CONFLICT (role_id, menu_id) DO NOTHING;
