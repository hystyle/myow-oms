-- Customer blacklist permission seed.

INSERT INTO sys_menu(menu_id, menu_name, parent_id, sort, path, component, is_frame, is_cache, menu_type, visible, status, api_perms, icon, create_time, update_time, deleted_flag)
VALUES
    (4400, 'Customer Blacklist', 4000, 4, '/customer/blacklist', 'customer/blacklist/index', '1', '0', 'C', '0', '0', 'customer:blacklist:list', 'shield-alert', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4401, 'Blacklist Create', 4400, 1, null, null, '1', '0', 'F', '0', '0', 'customer:blacklist:create', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4402, 'Blacklist Update', 4400, 2, null, null, '1', '0', 'F', '0', '0', 'customer:blacklist:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4403, 'Blacklist Delete', 4400, 3, null, null, '1', '0', 'F', '0', '0', 'customer:blacklist:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4404, 'Blacklist Check', 4400, 4, null, null, '1', '0', 'F', '0', '0', 'customer:blacklist:check', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false)
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
WHERE menu_id IN (4400, 4401, 4402, 4403, 4404)
ON CONFLICT (role_id, menu_id) DO NOTHING;
