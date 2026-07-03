-- Customer blacklist for customer, tax number, license number, phone, and email strong interception.

CREATE TABLE IF NOT EXISTS cm_customer_blacklist (
    blacklist_id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    target_type VARCHAR(32) NOT NULL,
    target_value VARCHAR(128) NOT NULL,
    risk_level VARCHAR(16) DEFAULT 'HIGH',
    reason VARCHAR(512) NOT NULL,
    source_customer_id BIGINT,
    status VARCHAR(16) DEFAULT 'ACTIVE',
    effective_time TIMESTAMP(3),
    expire_time TIMESTAMP(3),
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE,
    CONSTRAINT uk_cm_blacklist_target UNIQUE (tenant_id, target_type, target_value)
);

CREATE INDEX IF NOT EXISTS idx_cm_blacklist_target
    ON cm_customer_blacklist (tenant_id, target_type, target_value, status);
CREATE INDEX IF NOT EXISTS idx_cm_blacklist_source_customer
    ON cm_customer_blacklist (tenant_id, source_customer_id);

COMMENT ON TABLE cm_customer_blacklist IS 'Customer domain blacklist';
COMMENT ON COLUMN cm_customer_blacklist.target_type IS 'CUSTOMER_ID / TAX_NO / LICENSE_NO / PHONE / EMAIL';
COMMENT ON COLUMN cm_customer_blacklist.risk_level IS 'LOW / MEDIUM / HIGH / CRITICAL';
COMMENT ON COLUMN cm_customer_blacklist.status IS 'ACTIVE / DISABLED';
