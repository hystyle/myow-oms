-- Complete first-phase user-center management permissions for local/dev bootstrap.

INSERT INTO sys_menu(menu_id, menu_name, parent_id, sort, path, component, is_frame, is_cache, menu_type, visible, status, api_perms, icon, create_time, update_time, deleted_flag)
VALUES
    (1201, 'Role Add', 1200, 1, null, null, '1', '0', 'F', '0', '0', 'system:role:add', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1202, 'Role Update', 1200, 2, null, null, '1', '0', 'F', '0', '0', 'system:role:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1203, 'Role Delete', 1200, 3, null, null, '1', '0', 'F', '0', '0', 'system:role:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1301, 'Menu Add', 1300, 1, null, null, '1', '0', 'F', '0', '0', 'system:menu:add', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1302, 'Menu Update', 1300, 2, null, null, '1', '0', 'F', '0', '0', 'system:menu:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1303, 'Menu Delete', 1300, 3, null, null, '1', '0', 'F', '0', '0', 'system:menu:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1401, 'Dept Add', 1400, 1, null, null, '1', '0', 'F', '0', '0', 'system:dept:add', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1402, 'Dept Update', 1400, 2, null, null, '1', '0', 'F', '0', '0', 'system:dept:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1403, 'Dept Delete', 1400, 3, null, null, '1', '0', 'F', '0', '0', 'system:dept:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1501, 'Position Add', 1500, 1, null, null, '1', '0', 'F', '0', '0', 'system:position:add', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1502, 'Position Update', 1500, 2, null, null, '1', '0', 'F', '0', '0', 'system:position:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1503, 'Position Delete', 1500, 3, null, null, '1', '0', 'F', '0', '0', 'system:position:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1601, 'Config Add', 1600, 1, null, null, '1', '0', 'F', '0', '0', 'system:config:add', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1602, 'Config Update', 1600, 2, null, null, '1', '0', 'F', '0', '0', 'system:config:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1603, 'Config Delete', 1600, 3, null, null, '1', '0', 'F', '0', '0', 'system:config:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1700, 'Dict', 1000, 7, '/system/dict', 'system/dict/index', '1', '0', 'C', '0', '0', 'system:dict:query', 'book', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1701, 'Dict Add', 1700, 1, null, null, '1', '0', 'F', '0', '0', 'system:dict:add', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1702, 'Dict Update', 1700, 2, null, null, '1', '0', 'F', '0', '0', 'system:dict:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1703, 'Dict Delete', 1700, 3, null, null, '1', '0', 'F', '0', '0', 'system:dict:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false)
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
WHERE menu_id IN (
    1201, 1202, 1203,
    1301, 1302, 1303,
    1401, 1402, 1403,
    1501, 1502, 1503,
    1601, 1602, 1603,
    1700, 1701, 1702, 1703
)
ON CONFLICT (role_id, menu_id) DO NOTHING;
