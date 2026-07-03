-- Customer role tags for customer/supplier/agent/carrier/warehouse provider identity reuse.

CREATE TABLE IF NOT EXISTS cm_customer_role (
    customer_role_id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    role_type VARCHAR(32) NOT NULL,
    role_status VARCHAR(16) DEFAULT 'ACTIVE',
    role_code VARCHAR(32),
    offset_enabled BOOLEAN DEFAULT FALSE,
    remark VARCHAR(512),
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE,
    CONSTRAINT uk_cm_customer_role UNIQUE (tenant_id, customer_id, role_type)
);

CREATE INDEX IF NOT EXISTS idx_cm_customer_role_customer
    ON cm_customer_role (tenant_id, customer_id);
CREATE INDEX IF NOT EXISTS idx_cm_customer_role_type
    ON cm_customer_role (tenant_id, role_type, role_status);

COMMENT ON TABLE cm_customer_role IS 'Customer business role tags';
COMMENT ON COLUMN cm_customer_role.role_type IS 'CUSTOMER / SUPPLIER / OVERSEAS_AGENT / CARRIER / WAREHOUSE_PROVIDER / CUSTOMS_BROKER';
COMMENT ON COLUMN cm_customer_role.role_code IS 'Optional external or finance code for this role';
COMMENT ON COLUMN cm_customer_role.offset_enabled IS 'Whether finance can offset receivable and payable for this customer';
