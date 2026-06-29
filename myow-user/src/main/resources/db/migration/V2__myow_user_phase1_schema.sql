-- Phase 1 user-center schema alignment for PostgreSQL.

ALTER TABLE sys_user
    ADD COLUMN IF NOT EXISTS failed_login_count INT DEFAULT 0,
    ADD COLUMN IF NOT EXISTS locked_until TIMESTAMP(3),
    ADD COLUMN IF NOT EXISTS password_update_time TIMESTAMP(3),
    ADD COLUMN IF NOT EXISTS password_expire_time TIMESTAMP(3),
    ADD COLUMN IF NOT EXISTS must_change_password BOOLEAN DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS last_login_time TIMESTAMP(3),
    ADD COLUMN IF NOT EXISTS last_login_ip VARCHAR(64);

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'sys_user_post' AND column_name = 'post_id'
    ) AND NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'sys_user_post' AND column_name = 'position_id'
    ) THEN
        ALTER TABLE sys_user_post RENAME COLUMN post_id TO position_id;
    END IF;
END $$;

CREATE TABLE IF NOT EXISTS sys_config (
    config_id    BIGINT PRIMARY KEY,
    tenant_id    BIGINT NOT NULL DEFAULT 0,
    config_key   VARCHAR(128) NOT NULL,
    config_value VARCHAR(512),
    config_type  VARCHAR(32) DEFAULT 'STRING',
    group_code   VARCHAR(64),
    system_flag  BOOLEAN DEFAULT FALSE,
    remark       VARCHAR(512),
    create_by    BIGINT,
    create_time  TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by    BIGINT,
    update_time  TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE,
    CONSTRAINT uk_sys_config_key UNIQUE (tenant_id, config_key)
);

CREATE INDEX IF NOT EXISTS idx_sys_config_group ON sys_config(group_code);

CREATE TABLE IF NOT EXISTS sys_login_log (
    login_log_id   BIGINT PRIMARY KEY,
    tenant_id      BIGINT,
    user_id        BIGINT,
    login_name     VARCHAR(64) NOT NULL,
    login_type     VARCHAR(32) DEFAULT 'PASSWORD',
    login_client   VARCHAR(32),
    login_ip       VARCHAR(64),
    login_location VARCHAR(128),
    user_agent     VARCHAR(512),
    status         SMALLINT DEFAULT 0,
    fail_reason    VARCHAR(256),
    login_time     TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    trace_id       VARCHAR(64)
);

CREATE INDEX IF NOT EXISTS idx_login_log_tenant_time ON sys_login_log(tenant_id, login_time);
CREATE INDEX IF NOT EXISTS idx_login_log_name_time ON sys_login_log(login_name, login_time);

INSERT INTO sys_config(config_id, tenant_id, config_key, config_value, config_type, group_code, system_flag, remark)
VALUES
    (10001, 0, 'myow.tenant.enabled', 'false', 'BOOLEAN', 'tenant', true, 'Enable tenant isolation'),
    (10002, 0, 'security.password.min-length', '8', 'NUMBER', 'security', true, 'Minimum password length'),
    (10003, 0, 'security.password.require-letter', 'true', 'BOOLEAN', 'security', true, 'Password must contain letter'),
    (10004, 0, 'security.password.require-number', 'true', 'BOOLEAN', 'security', true, 'Password must contain number'),
    (10005, 0, 'security.password.expire-days', '90', 'NUMBER', 'security', true, 'Password expiration days'),
    (10006, 0, 'security.login.max-fail-count', '5', 'NUMBER', 'security', true, 'Max failed login count'),
    (10007, 0, 'security.login.lock-minutes', '30', 'NUMBER', 'security', true, 'Account lock minutes'),
    (10008, 0, 'security.login.captcha-enabled', 'false', 'BOOLEAN', 'security', true, 'Require captcha on login'),
    (10009, 0, 'security.login.captcha-after-fail-count', '3', 'NUMBER', 'security', true, 'Failed count before captcha'),
    (10010, 0, 'security.session.multi-login', 'true', 'BOOLEAN', 'security', true, 'Allow multiple sessions'),
    (10011, 0, 'security.session.token-timeout-seconds', '7200', 'NUMBER', 'security', true, 'Token timeout seconds'),
    (10012, 0, 'security.session.refresh-window-seconds', '1800', 'NUMBER', 'security', true, 'Token refresh window seconds'),
    (10013, 0, 'audit.oper-log.retention-days', '180', 'NUMBER', 'audit', true, 'Operation log retention days'),
    (10014, 0, 'audit.login-log.retention-days', '180', 'NUMBER', 'audit', true, 'Login log retention days')
ON CONFLICT (tenant_id, config_key) DO NOTHING;
