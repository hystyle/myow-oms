-- Customer attachment and KYC permission seed.

INSERT INTO sys_menu(menu_id, menu_name, parent_id, sort, path, component, is_frame, is_cache, menu_type, visible, status, api_perms, icon, create_time, update_time, deleted_flag)
VALUES
    (4112, 'Customer Attachment List', 4100, 12, null, null, '1', '0', 'F', '0', '0', 'customer:attachment:list', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4113, 'Customer Attachment Create', 4100, 13, null, null, '1', '0', 'F', '0', '0', 'customer:attachment:create', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4114, 'Customer Attachment Update', 4100, 14, null, null, '1', '0', 'F', '0', '0', 'customer:attachment:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4115, 'Customer Attachment Delete', 4100, 15, null, null, '1', '0', 'F', '0', '0', 'customer:attachment:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4116, 'Customer KYC List', 4100, 16, null, null, '1', '0', 'F', '0', '0', 'customer:kyc:list', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4117, 'Customer KYC Create', 4100, 17, null, null, '1', '0', 'F', '0', '0', 'customer:kyc:create', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4118, 'Customer KYC Update', 4100, 18, null, null, '1', '0', 'F', '0', '0', 'customer:kyc:update', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4119, 'Customer KYC Audit', 4100, 19, null, null, '1', '0', 'F', '0', '0', 'customer:kyc:audit', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (4120, 'Customer KYC Delete', 4100, 20, null, null, '1', '0', 'F', '0', '0', 'customer:kyc:delete', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false)
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
WHERE menu_id IN (4112, 4113, 4114, 4115, 4116, 4117, 4118, 4119, 4120)
ON CONFLICT (role_id, menu_id) DO NOTHING;
