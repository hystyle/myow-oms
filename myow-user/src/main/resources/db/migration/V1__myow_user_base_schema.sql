-- User-center base schema for PostgreSQL.

CREATE TABLE IF NOT EXISTS sys_user (
    user_id BIGINT PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL DEFAULT '1',
    user_code VARCHAR(64),
    dept_id BIGINT,
    login_name VARCHAR(64) NOT NULL,
    position_id BIGINT,
    nick_name VARCHAR(64),
    user_type VARCHAR(32),
    email VARCHAR(128),
    phone VARCHAR(32),
    gender VARCHAR(8),
    avatar BIGINT,
    password VARCHAR(128),
    status BOOLEAN DEFAULT TRUE,
    admin_flag BOOLEAN DEFAULT FALSE,
    failed_login_count INT DEFAULT 0,
    locked_until TIMESTAMP(3),
    password_update_time TIMESTAMP(3),
    password_expire_time TIMESTAMP(3),
    must_change_password BOOLEAN DEFAULT FALSE,
    last_login_time TIMESTAMP(3),
    last_login_ip VARCHAR(64),
    deleted_flag BOOLEAN DEFAULT FALSE,
    create_dept BIGINT,
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    remark VARCHAR(512),
    CONSTRAINT uk_sys_user_login_name UNIQUE (tenant_id, login_name)
);

CREATE TABLE IF NOT EXISTS sys_dept (
    dept_id BIGINT PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL DEFAULT '1',
    parent_id BIGINT DEFAULT 0,
    dept_name VARCHAR(64) NOT NULL,
    sort INT DEFAULT 0,
    manager_id BIGINT,
    deleted_flag BOOLEAN DEFAULT FALSE,
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3)
);

CREATE TABLE IF NOT EXISTS sys_role (
    role_id BIGINT PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL DEFAULT '1',
    role_name VARCHAR(64) NOT NULL,
    role_code VARCHAR(64) NOT NULL,
    sort INT DEFAULT 0,
    data_scope SMALLINT DEFAULT 5,
    menu_check_strictly BOOLEAN DEFAULT TRUE,
    dept_check_strictly BOOLEAN DEFAULT TRUE,
    status VARCHAR(8) DEFAULT '0',
    deleted_flag BOOLEAN DEFAULT FALSE,
    create_dept BIGINT,
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    remark VARCHAR(512),
    CONSTRAINT uk_sys_role_code UNIQUE (tenant_id, role_code)
);

CREATE TABLE IF NOT EXISTS sys_menu (
    menu_id BIGINT PRIMARY KEY,
    menu_name VARCHAR(64) NOT NULL,
    parent_id BIGINT DEFAULT 0,
    sort INT DEFAULT 0,
    path VARCHAR(256),
    component VARCHAR(256),
    query_param VARCHAR(512),
    is_frame VARCHAR(8) DEFAULT '1',
    is_cache VARCHAR(8) DEFAULT '0',
    menu_type VARCHAR(8) NOT NULL,
    visible VARCHAR(8) DEFAULT '0',
    status VARCHAR(8) DEFAULT '0',
    api_perms VARCHAR(256),
    icon VARCHAR(128),
    create_dept BIGINT,
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    remark VARCHAR(512),
    deleted_flag BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS sys_role_menu (
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, menu_id)
);

CREATE TABLE IF NOT EXISTS sys_role_dept (
    role_id BIGINT NOT NULL,
    dept_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, dept_id)
);

CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS sys_position (
    position_id BIGINT PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL DEFAULT '1',
    dept_id BIGINT,
    position_code VARCHAR(64) NOT NULL,
    position_name VARCHAR(64) NOT NULL,
    sort INT DEFAULT 0,
    status VARCHAR(8) DEFAULT '0',
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    remark VARCHAR(512),
    CONSTRAINT uk_sys_position_code UNIQUE (tenant_id, position_code)
);

CREATE TABLE IF NOT EXISTS sys_user_post (
    user_id BIGINT NOT NULL,
    position_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, position_id)
);

CREATE TABLE IF NOT EXISTS sys_tenant_plans (
    plans_id BIGINT PRIMARY KEY,
    plans_name VARCHAR(64) NOT NULL,
    plans_code VARCHAR(64),
    price_type VARCHAR(32),
    status VARCHAR(8) DEFAULT '0',
    deleted_flag BOOLEAN DEFAULT FALSE,
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    CONSTRAINT uk_sys_tenant_plans_code UNIQUE (plans_code)
);

CREATE TABLE IF NOT EXISTS sys_tenant (
    tenant_id BIGINT PRIMARY KEY,
    tenant_code VARCHAR(64) NOT NULL,
    name VARCHAR(128) NOT NULL,
    plans_id BIGINT,
    expire_time TIMESTAMP(3),
    account_count INT DEFAULT -1,
    status BOOLEAN DEFAULT TRUE,
    contact_name VARCHAR(64),
    contact_phone VARCHAR(32),
    address VARCHAR(256),
    license_number VARCHAR(64),
    intro VARCHAR(512),
    domain VARCHAR(128),
    remark VARCHAR(512),
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE,
    CONSTRAINT uk_sys_tenant_code UNIQUE (tenant_code)
);

CREATE TABLE IF NOT EXISTS sys_dict (
    dict_id BIGINT PRIMARY KEY,
    dict_name VARCHAR(64) NOT NULL,
    dict_code VARCHAR(64) NOT NULL,
    remark VARCHAR(512),
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by BIGINT,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    CONSTRAINT uk_sys_dict_code UNIQUE (dict_code)
);

CREATE TABLE IF NOT EXISTS sys_dict_data (
    dict_data_id BIGINT PRIMARY KEY,
    dict_id BIGINT NOT NULL,
    data_value VARCHAR(128) NOT NULL,
    data_label VARCHAR(128) NOT NULL,
    remark VARCHAR(512),
    sort INT DEFAULT 0,
    disabled_flag BOOLEAN DEFAULT FALSE,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3)
);

CREATE TABLE IF NOT EXISTS sys_serial_no_config (
    serial_number_id INT PRIMARY KEY,
    business_name VARCHAR(64) NOT NULL,
    format VARCHAR(128) NOT NULL,
    rule_type VARCHAR(32) NOT NULL,
    remark VARCHAR(512),
    init_number BIGINT DEFAULT 1,
    step_random_range INT,
    last_number BIGINT,
    last_time TIMESTAMP(3),
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3)
);

CREATE TABLE IF NOT EXISTS sys_serial_no_record (
    serial_number_id INT NOT NULL,
    record_date DATE NOT NULL,
    last_number BIGINT DEFAULT 0,
    last_time TIMESTAMP(3),
    count BIGINT DEFAULT 0,
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    PRIMARY KEY (serial_number_id, record_date)
);

CREATE TABLE IF NOT EXISTS t_i18n_key (
    id BIGINT PRIMARY KEY,
    key_code VARCHAR(128) NOT NULL,
    biz_domain VARCHAR(64),
    description VARCHAR(512),
    status SMALLINT DEFAULT 0,
    created_by VARCHAR(64),
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    CONSTRAINT uk_i18n_key_code UNIQUE (key_code)
);

CREATE TABLE IF NOT EXISTS t_i18n_message (
    id BIGINT PRIMARY KEY,
    key_code VARCHAR(128) NOT NULL,
    lang VARCHAR(32) NOT NULL,
    message TEXT NOT NULL,
    version INT DEFAULT 1,
    status SMALLINT DEFAULT 0,
    created_by VARCHAR(64),
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    CONSTRAINT uk_i18n_message_key_lang UNIQUE (key_code, lang)
);

CREATE TABLE IF NOT EXISTS sys_oper_log (
    oper_id BIGINT PRIMARY KEY,
    tenant_id VARCHAR(64) DEFAULT '1',
    title VARCHAR(128),
    business_type INT,
    method VARCHAR(256),
    request_method VARCHAR(16),
    operator_type INT,
    oper_name VARCHAR(64),
    dept_name VARCHAR(64),
    oper_url VARCHAR(256),
    oper_ip VARCHAR(64),
    oper_location VARCHAR(128),
    oper_param TEXT,
    json_result TEXT,
    status INT DEFAULT 0,
    error_msg TEXT,
    oper_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    cost_time BIGINT
);

CREATE INDEX IF NOT EXISTS idx_sys_user_tenant_id ON sys_user(tenant_id);
CREATE INDEX IF NOT EXISTS idx_sys_dept_parent_id ON sys_dept(parent_id);
CREATE INDEX IF NOT EXISTS idx_sys_role_tenant_id ON sys_role(tenant_id);
CREATE INDEX IF NOT EXISTS idx_sys_menu_parent_id ON sys_menu(parent_id);
CREATE INDEX IF NOT EXISTS idx_sys_dict_data_dict_id ON sys_dict_data(dict_id);
CREATE INDEX IF NOT EXISTS idx_sys_oper_log_tenant_time ON sys_oper_log(tenant_id, oper_time);
