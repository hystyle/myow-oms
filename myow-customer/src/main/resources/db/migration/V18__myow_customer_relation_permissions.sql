-- Customer relation permission seed.

INSERT INTO sys_menu(menu_id, menu_name, parent_id, sort, path, component, is_frame, is_cache, menu_type, visible, status, api_perms, icon, create_time, update_time, deleted_flag)
VALUES
    (4108, 'Customer Relation List', 4100, 8, null, null, '1', '0', 'F', '0', '0', 'customer:relation:list', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4109, 'Customer Relation Create', 4100, 9, null, null, '1', '0', 'F', '0', '0', 'customer:relation:create', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4110, 'Customer Relation Update', 4100, 10, null, null, '1', '0', 'F', '0', '0', 'customer:relation:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4111, 'Customer Relation Delete', 4100, 11, null, null, '1', '0', 'F', '0', '0', 'customer:relation:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false)
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
WHERE menu_id IN (4108, 4109, 4110, 4111)
ON CONFLICT (role_id, menu_id) DO NOTHING;
