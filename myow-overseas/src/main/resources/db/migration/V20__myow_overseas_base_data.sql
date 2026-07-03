-- Overseas warehouse base data: physical warehouse, logistics product and channel.

CREATE TABLE IF NOT EXISTS owh_physical_warehouse (
    warehouse_id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    warehouse_code VARCHAR(32) NOT NULL,
    warehouse_name VARCHAR(128) NOT NULL,
    service_provider_customer_id BIGINT NOT NULL,
    cooperation_type VARCHAR(32),
    wms_system_id BIGINT,
    external_warehouse_code VARCHAR(64),
    country_code VARCHAR(8) NOT NULL,
    state VARCHAR(64),
    city VARCHAR(64),
    postal_code VARCHAR(32),
    address_line1 VARCHAR(256),
    address_line2 VARCHAR(256),
    contact_name VARCHAR(64),
    contact_phone VARCHAR(32),
    contact_email VARCHAR(128),
    timezone VARCHAR(64) NOT NULL,
    status VARCHAR(16) NOT NULL DEFAULT 'DRAFT',
    remark VARCHAR(512),
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE,
    CONSTRAINT uk_owh_physical_warehouse_code UNIQUE (tenant_id, warehouse_code)
);
CREATE INDEX IF NOT EXISTS idx_owh_physical_warehouse_provider
    ON owh_physical_warehouse(tenant_id, service_provider_customer_id);
CREATE INDEX IF NOT EXISTS idx_owh_physical_warehouse_status
    ON owh_physical_warehouse(tenant_id, country_code, status);
COMMENT ON TABLE owh_physical_warehouse IS 'Physical warehouse base data';
COMMENT ON COLUMN owh_physical_warehouse.service_provider_customer_id IS 'Customer id with WAREHOUSE_PROVIDER role';

CREATE TABLE IF NOT EXISTS owh_logistics_product (
    product_id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    product_code VARCHAR(32) NOT NULL,
    product_name VARCHAR(128) NOT NULL,
    carrier_customer_id BIGINT NOT NULL,
    product_type VARCHAR(32) NOT NULL,
    default_channel_id BIGINT,
    default_decision_strategy VARCHAR(32),
    status VARCHAR(16) NOT NULL DEFAULT 'DRAFT',
    remark VARCHAR(512),
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE,
    CONSTRAINT uk_owh_logistics_product_code UNIQUE (tenant_id, product_code)
);
CREATE INDEX IF NOT EXISTS idx_owh_logistics_product_carrier
    ON owh_logistics_product(tenant_id, carrier_customer_id);
CREATE INDEX IF NOT EXISTS idx_owh_logistics_product_status
    ON owh_logistics_product(tenant_id, product_type, status);
COMMENT ON TABLE owh_logistics_product IS 'Logistics product base data';
COMMENT ON COLUMN owh_logistics_product.carrier_customer_id IS 'Customer id with CARRIER role';

CREATE TABLE IF NOT EXISTS owh_logistics_channel (
    channel_id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    channel_code VARCHAR(32) NOT NULL,
    channel_name VARCHAR(128) NOT NULL,
    carrier_customer_id BIGINT NOT NULL,
    channel_type VARCHAR(32),
    label_source VARCHAR(32) NOT NULL,
    tms_system_id BIGINT,
    label_format VARCHAR(16) DEFAULT 'PDF',
    status VARCHAR(16) NOT NULL DEFAULT 'DRAFT',
    remark VARCHAR(512),
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE,
    CONSTRAINT uk_owh_logistics_channel_code UNIQUE (tenant_id, channel_code)
);
CREATE INDEX IF NOT EXISTS idx_owh_logistics_channel_carrier
    ON owh_logistics_channel(tenant_id, carrier_customer_id);
CREATE INDEX IF NOT EXISTS idx_owh_logistics_channel_status
    ON owh_logistics_channel(tenant_id, label_source, status);
COMMENT ON TABLE owh_logistics_channel IS 'Logistics channel base data';
COMMENT ON COLUMN owh_logistics_channel.carrier_customer_id IS 'Customer id with CARRIER role';

INSERT INTO sys_menu(menu_id, menu_name, parent_id, sort, path, component, is_frame, is_cache, menu_type, visible, status, api_perms, icon, create_time, update_time, deleted_flag)
VALUES
    (5000, 'Overseas Base Data', 0, 50, '/overseas/base', null, '1', '0', 'M', '0', '0', 'overseas:base:view', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (5001, 'Physical Warehouse List', 5000, 1, null, null, '1', '0', 'F', '0', '0', 'overseas:base:physical-warehouse:list', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (5002, 'Physical Warehouse Manage', 5000, 2, null, null, '1', '0', 'F', '0', '0', 'overseas:base:physical-warehouse:manage', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (5003, 'Logistics Product List', 5000, 3, null, null, '1', '0', 'F', '0', '0', 'overseas:base:logistics-product:list', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (5004, 'Logistics Product Manage', 5000, 4, null, null, '1', '0', 'F', '0', '0', 'overseas:base:logistics-product:manage', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (5005, 'Logistics Channel List', 5000, 5, null, null, '1', '0', 'F', '0', '0', 'overseas:base:logistics-channel:list', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (5006, 'Logistics Channel Manage', 5000, 6, null, null, '1', '0', 'F', '0', '0', 'overseas:base:logistics-channel:manage', null, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false)
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
WHERE menu_id IN (5000, 5001, 5002, 5003, 5004, 5005, 5006)
ON CONFLICT (role_id, menu_id) DO NOTHING;
