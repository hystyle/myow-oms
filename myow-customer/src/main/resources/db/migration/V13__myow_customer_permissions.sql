-- Customer module menu and permission seed for admin bootstrap.

INSERT INTO sys_menu(menu_id, menu_name, parent_id, sort, path, component, is_frame, is_cache, menu_type, visible, status, api_perms, icon, create_time, update_time, deleted_flag)
VALUES
    (4000, 'Customer Center', 0, 40, '/customer', null, '1', '0', 'M', '0', '0', null, 'users', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4100, 'Customers', 4000, 1, '/customer/customers', 'customer/customers/index', '1', '0', 'C', '0', '0', 'customer:customer:list', 'user-round', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4101, 'Customer Create', 4100, 1, null, null, '1', '0', 'F', '0', '0', 'customer:customer:create', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4102, 'Customer Update', 4100, 2, null, null, '1', '0', 'F', '0', '0', 'customer:customer:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4103, 'Customer Delete', 4100, 3, null, null, '1', '0', 'F', '0', '0', 'customer:customer:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4200, 'Customer Contacts', 4000, 2, '/customer/contacts', 'customer/contacts/index', '1', '0', 'C', '0', '0', 'customer:contact:list', 'contact', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4201, 'Contact Create', 4200, 1, null, null, '1', '0', 'F', '0', '0', 'customer:contact:create', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4202, 'Contact Update', 4200, 2, null, null, '1', '0', 'F', '0', '0', 'customer:contact:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4203, 'Contact Delete', 4200, 3, null, null, '1', '0', 'F', '0', '0', 'customer:contact:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4300, 'Customer Addresses', 4000, 3, '/customer/addresses', 'customer/addresses/index', '1', '0', 'C', '0', '0', 'customer:address:list', 'map-pin', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4301, 'Address Create', 4300, 1, null, null, '1', '0', 'F', '0', '0', 'customer:address:create', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4302, 'Address Update', 4300, 2, null, null, '1', '0', 'F', '0', '0', 'customer:address:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4303, 'Address Delete', 4300, 3, null, null, '1', '0', 'F', '0', '0', 'customer:address:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false)
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
    4000,
    4100, 4101, 4102, 4103,
    4200, 4201, 4202, 4203,
    4300, 4301, 4302, 4303
)
ON CONFLICT (role_id, menu_id) DO NOTHING;
