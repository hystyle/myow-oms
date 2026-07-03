-- Customer core schema for PostgreSQL.
-- P0 scope: customer, contact, address, settlement profile,
-- customer relation, attachment index, and KYC audit records.

CREATE TABLE IF NOT EXISTS cm_customer (
    customer_id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    customer_code VARCHAR(32) NOT NULL,
    customer_name VARCHAR(128) NOT NULL,
    customer_type VARCHAR(16) NOT NULL DEFAULT 'COMPANY',
    customer_level VARCHAR(16) DEFAULT 'BRONZE',
    biz_license_no VARCHAR(64),
    tax_no VARCHAR(64),
    settlement_type VARCHAR(16) DEFAULT 'PREPAID',
    default_currency VARCHAR(8) DEFAULT 'USD',
    status VARCHAR(16) DEFAULT 'PENDING',
    sales_owner_id BIGINT,
    owner_dept_id BIGINT,
    pool_status VARCHAR(16) DEFAULT 'PRIVATE',
    register_time TIMESTAMP(3),
    audit_time TIMESTAMP(3),
    remark VARCHAR(512),
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE,
    CONSTRAINT uk_cm_customer_code UNIQUE (tenant_id, customer_code)
);

CREATE INDEX IF NOT EXISTS idx_cm_customer_status
    ON cm_customer (tenant_id, status);
CREATE INDEX IF NOT EXISTS idx_cm_customer_level
    ON cm_customer (tenant_id, customer_level);
CREATE INDEX IF NOT EXISTS idx_cm_customer_owner
    ON cm_customer (tenant_id, sales_owner_id, owner_dept_id, pool_status);

COMMENT ON TABLE cm_customer IS 'Customer and trading partner master data';
COMMENT ON COLUMN cm_customer.status IS 'PENDING / ACTIVE / SUSPENDED / TERMINATED';
COMMENT ON COLUMN cm_customer.pool_status IS 'PRIVATE / PUBLIC';

CREATE TABLE IF NOT EXISTS cm_customer_contact (
    contact_id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    contact_name VARCHAR(64) NOT NULL,
    contact_role VARCHAR(32),
    position VARCHAR(32),
    phone VARCHAR(32),
    email VARCHAR(128),
    social_account VARCHAR(128),
    is_primary BOOLEAN DEFAULT FALSE,
    status SMALLINT DEFAULT 1,
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_cm_contact_customer
    ON cm_customer_contact (tenant_id, customer_id);
CREATE INDEX IF NOT EXISTS idx_cm_contact_role
    ON cm_customer_contact (tenant_id, customer_id, contact_role);
CREATE INDEX IF NOT EXISTS idx_cm_contact_phone
    ON cm_customer_contact (tenant_id, phone);
CREATE INDEX IF NOT EXISTS idx_cm_contact_email
    ON cm_customer_contact (tenant_id, email);

COMMENT ON TABLE cm_customer_contact IS 'Customer contact matrix';
COMMENT ON COLUMN cm_customer_contact.contact_role IS 'BUSINESS / FINANCE / TECH / WAREHOUSE / LEGAL / MANAGER';

CREATE TABLE IF NOT EXISTS cm_customer_address (
    address_id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    address_type VARCHAR(32) NOT NULL,
    contact_name VARCHAR(64),
    phone VARCHAR(32),
    country VARCHAR(64),
    country_code VARCHAR(8),
    province VARCHAR(64),
    city VARCHAR(64),
    district VARCHAR(64),
    street VARCHAR(256),
    zip_code VARCHAR(16),
    is_default BOOLEAN DEFAULT FALSE,
    status SMALLINT DEFAULT 1,
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_cm_address_customer
    ON cm_customer_address (tenant_id, customer_id, address_type);
CREATE INDEX IF NOT EXISTS idx_cm_address_default
    ON cm_customer_address (tenant_id, customer_id, address_type, is_default);

COMMENT ON TABLE cm_customer_address IS 'Customer address book';
COMMENT ON COLUMN cm_customer_address.address_type IS 'REGISTERED / SHIP_FROM / RETURN_TO / BILLING / WAREHOUSE_CONTACT / OTHER';

CREATE TABLE IF NOT EXISTS cm_customer_settlement_profile (
    profile_id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    settlement_type VARCHAR(16) DEFAULT 'PREPAID',
    default_currency VARCHAR(8) DEFAULT 'USD',
    payment_terms VARCHAR(32),
    invoice_title VARCHAR(128),
    tax_no VARCHAR(64),
    finance_subject_code VARCHAR(64),
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE,
    CONSTRAINT uk_cm_settlement_customer UNIQUE (tenant_id, customer_id)
);

CREATE INDEX IF NOT EXISTS idx_cm_settlement_customer
    ON cm_customer_settlement_profile (tenant_id, customer_id);

COMMENT ON TABLE cm_customer_settlement_profile IS 'Customer settlement preferences';
COMMENT ON COLUMN cm_customer_settlement_profile.settlement_type IS 'PREPAID / CREDIT / MONTHLY';

CREATE TABLE IF NOT EXISTS cm_customer_relation (
    relation_id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    parent_customer_id BIGINT NOT NULL,
    child_customer_id BIGINT NOT NULL,
    relation_type VARCHAR(32) NOT NULL,
    settlement_independent BOOLEAN DEFAULT FALSE,
    status SMALLINT DEFAULT 1,
    remark VARCHAR(512),
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE,
    CONSTRAINT uk_cm_customer_relation UNIQUE (tenant_id, parent_customer_id, child_customer_id, relation_type)
);

CREATE INDEX IF NOT EXISTS idx_cm_relation_parent
    ON cm_customer_relation (tenant_id, parent_customer_id);
CREATE INDEX IF NOT EXISTS idx_cm_relation_child
    ON cm_customer_relation (tenant_id, child_customer_id);

COMMENT ON TABLE cm_customer_relation IS 'Customer parent-child and billing-title relations';
COMMENT ON COLUMN cm_customer_relation.relation_type IS 'PARENT_CHILD / BILLING_TITLE / SETTLEMENT_SUBJECT';

CREATE TABLE IF NOT EXISTS cm_customer_attachment (
    attachment_id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    attachment_type VARCHAR(32) NOT NULL,
    file_id BIGINT NOT NULL,
    file_name VARCHAR(256),
    expire_date DATE,
    audit_status VARCHAR(16) DEFAULT 'PENDING',
    remark VARCHAR(512),
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_cm_attachment_customer
    ON cm_customer_attachment (tenant_id, customer_id, attachment_type);
CREATE INDEX IF NOT EXISTS idx_cm_attachment_expire
    ON cm_customer_attachment (tenant_id, expire_date);

COMMENT ON TABLE cm_customer_attachment IS 'Customer attachment index';
COMMENT ON COLUMN cm_customer_attachment.attachment_type IS 'CONTRACT_COPY / LICENSE / TAX_FILE / KYC_FILE / OTHER';

CREATE TABLE IF NOT EXISTS cm_customer_kyc (
    kyc_id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    kyc_type VARCHAR(32) NOT NULL,
    audit_status VARCHAR(16) DEFAULT 'PENDING',
    audit_by BIGINT,
    audit_time TIMESTAMP(3),
    reject_reason VARCHAR(512),
    remark VARCHAR(512),
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_cm_kyc_customer
    ON cm_customer_kyc (tenant_id, customer_id, audit_status);
CREATE INDEX IF NOT EXISTS idx_cm_kyc_audit
    ON cm_customer_kyc (tenant_id, audit_status, audit_time);

COMMENT ON TABLE cm_customer_kyc IS 'Customer KYC and qualification audit records';
COMMENT ON COLUMN cm_customer_kyc.kyc_type IS 'COMPANY_LICENSE / PERSONAL_ID / TAX / COMPLIANCE';
