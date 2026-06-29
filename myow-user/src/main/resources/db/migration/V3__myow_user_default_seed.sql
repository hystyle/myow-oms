-- Default user-center seed data for local/dev bootstrap.

INSERT INTO sys_tenant_plans(plans_id, plans_name, plans_code, price_type, status, deleted_flag, create_time, update_time)
VALUES (1, 'Default Plan', 'DEFAULT', 'FREE', '0', false, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3))
ON CONFLICT (plans_id) DO UPDATE SET
    plans_name = EXCLUDED.plans_name,
    plans_code = EXCLUDED.plans_code,
    status = EXCLUDED.status,
    deleted_flag = false,
    update_time = CURRENT_TIMESTAMP(3);

INSERT INTO sys_tenant(tenant_id, tenant_code, name, plans_id, account_count, status, contact_name, remark, create_time, update_time, deleted_flag)
VALUES (1, 'default', 'Default Tenant', 1, -1, true, 'System', 'Default tenant for single-tenant startup', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false)
ON CONFLICT (tenant_id) DO UPDATE SET
    tenant_code = EXCLUDED.tenant_code,
    name = EXCLUDED.name,
    plans_id = EXCLUDED.plans_id,
    status = true,
    deleted_flag = false,
    update_time = CURRENT_TIMESTAMP(3);

INSERT INTO sys_dept(dept_id, tenant_id, parent_id, dept_name, sort, manager_id, deleted_flag, create_time, update_time)
VALUES (1, '1', 0, 'Headquarters', 1, 10001, false, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3))
ON CONFLICT (dept_id) DO UPDATE SET
    tenant_id = EXCLUDED.tenant_id,
    parent_id = EXCLUDED.parent_id,
    dept_name = EXCLUDED.dept_name,
    sort = EXCLUDED.sort,
    manager_id = EXCLUDED.manager_id,
    deleted_flag = false,
    update_time = CURRENT_TIMESTAMP(3);

INSERT INTO sys_position(position_id, tenant_id, dept_id, position_code, position_name, sort, status, create_time, update_time)
VALUES (1, '1', 1, 'ADMIN', 'Administrator', 1, '0', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3))
ON CONFLICT (position_id) DO UPDATE SET
    tenant_id = EXCLUDED.tenant_id,
    dept_id = EXCLUDED.dept_id,
    position_code = EXCLUDED.position_code,
    position_name = EXCLUDED.position_name,
    status = EXCLUDED.status,
    update_time = CURRENT_TIMESTAMP(3);

INSERT INTO sys_role(role_id, tenant_id, role_name, role_code, sort, data_scope, menu_check_strictly, dept_check_strictly, status, deleted_flag, create_time, update_time, remark)
VALUES (1, '1', 'Super Admin', 'SUPER_ADMIN', 1, 1, true, true, '0', false, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), 'Default super administrator role')
ON CONFLICT (role_id) DO UPDATE SET
    tenant_id = EXCLUDED.tenant_id,
    role_name = EXCLUDED.role_name,
    role_code = EXCLUDED.role_code,
    data_scope = EXCLUDED.data_scope,
    status = EXCLUDED.status,
    deleted_flag = false,
    update_time = CURRENT_TIMESTAMP(3);

INSERT INTO sys_user(
    user_id, tenant_id, user_code, dept_id, login_name, position_id, nick_name, user_type,
    email, phone, gender, password, status, admin_flag, failed_login_count, must_change_password,
    password_update_time, password_expire_time, deleted_flag, create_dept, create_time, update_time, remark
)
VALUES (
    10001, '1', 'U10001', 1, 'admin', 1, 'Administrator', 'sys_user',
    'admin@myow.local', '13800000000', '2', '7d67da6894d0c87aac6de62b82dec75d4ac45a51d870c23da973b57f9d78b24c',
    true, true, 0, false, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3) + INTERVAL '90 days',
    false, 1, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), 'Default administrator. Initial password: MyowAdmin2026!'
)
ON CONFLICT (user_id) DO UPDATE SET
    tenant_id = EXCLUDED.tenant_id,
    user_code = EXCLUDED.user_code,
    dept_id = EXCLUDED.dept_id,
    login_name = EXCLUDED.login_name,
    position_id = EXCLUDED.position_id,
    nick_name = EXCLUDED.nick_name,
    user_type = EXCLUDED.user_type,
    email = EXCLUDED.email,
    phone = EXCLUDED.phone,
    gender = EXCLUDED.gender,
    status = true,
    admin_flag = true,
    failed_login_count = 0,
    locked_until = null,
    must_change_password = false,
    deleted_flag = false,
    update_time = CURRENT_TIMESTAMP(3);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, sort, path, component, is_frame, is_cache, menu_type, visible, status, api_perms, icon, create_time, update_time, deleted_flag)
VALUES
    (1000, 'System', 0, 1, '/system', 'Layout', '1', '0', 'M', '0', '0', null, 'settings', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1100, 'User', 1000, 1, '/system/user', 'system/user/index', '1', '0', 'C', '0', '0', 'system:user:query', 'user', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1101, 'User Add', 1100, 1, null, null, '1', '0', 'F', '0', '0', 'system:user:add', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1102, 'User Update', 1100, 2, null, null, '1', '0', 'F', '0', '0', 'system:user:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1103, 'User Delete', 1100, 3, null, null, '1', '0', 'F', '0', '0', 'system:user:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1200, 'Role', 1000, 2, '/system/role', 'system/role/index', '1', '0', 'C', '0', '0', 'system:role:query', 'shield', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1300, 'Menu', 1000, 3, '/system/menu', 'system/menu/index', '1', '0', 'C', '0', '0', 'system:menu:query', 'menu', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1400, 'Dept', 1000, 4, '/system/dept', 'system/dept/index', '1', '0', 'C', '0', '0', 'system:dept:query', 'tree', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1500, 'Position', 1000, 5, '/system/position', 'system/position/index', '1', '0', 'C', '0', '0', 'system:position:query', 'id-card', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (1600, 'Config', 1000, 6, '/system/config', 'system/config/index', '1', '0', 'C', '0', '0', 'system:config:query', 'sliders', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false)
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

INSERT INTO sys_user_role(user_id, role_id)
VALUES (10001, 1)
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO sys_role_menu(role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE menu_id IN (1000, 1100, 1101, 1102, 1103, 1200, 1300, 1400, 1500, 1600)
ON CONFLICT (role_id, menu_id) DO NOTHING;
