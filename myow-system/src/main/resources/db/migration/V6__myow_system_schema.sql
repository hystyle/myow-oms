CREATE TABLE IF NOT EXISTS sys_job (
    job_id BIGINT PRIMARY KEY,
    job_name VARCHAR(128) NOT NULL,
    job_group VARCHAR(64) NOT NULL,
    cron_expression VARCHAR(128) NOT NULL,
    handler_name VARCHAR(128) NOT NULL,
    execute_policy VARCHAR(32) DEFAULT 'SKIP',
    concurrent BOOLEAN NOT NULL DEFAULT TRUE,
    status SMALLINT DEFAULT 1,
    remark VARCHAR(512),
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE,
    CONSTRAINT uk_sys_job_name_group UNIQUE (job_name, job_group)
);

CREATE TABLE IF NOT EXISTS sys_job_log (
    log_id BIGINT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    job_name VARCHAR(128) NOT NULL,
    job_group VARCHAR(64) NOT NULL,
    start_time TIMESTAMP(3) NOT NULL,
    end_time TIMESTAMP(3),
    cost_time BIGINT,
    status VARCHAR(16) NOT NULL,
    error_msg TEXT
);

CREATE TABLE IF NOT EXISTS sys_notice (
    notice_id BIGINT PRIMARY KEY,
    title VARCHAR(128) NOT NULL,
    content TEXT NOT NULL,
    notice_type VARCHAR(32) NOT NULL,
    status SMALLINT DEFAULT 0,
    publish_time TIMESTAMP(3),
    expire_time TIMESTAMP(3),
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP(3),
    deleted_flag BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS sys_notice_user (
    id BIGINT PRIMARY KEY,
    notice_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    read_status SMALLINT DEFAULT 0,
    read_time TIMESTAMP(3),
    CONSTRAINT uk_sys_notice_user UNIQUE (notice_id, user_id)
);

CREATE TABLE IF NOT EXISTS sys_file (
    file_id BIGINT PRIMARY KEY,
    module_name VARCHAR(64) NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    storage_type VARCHAR(32) DEFAULT 'LOCAL',
    storage_key VARCHAR(512) NOT NULL,
    file_size BIGINT NOT NULL,
    content_type VARCHAR(128),
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    deleted_flag BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS sys_site_config (
    config_id BIGINT PRIMARY KEY,
    site_code VARCHAR(64) NOT NULL,
    config_key VARCHAR(128) NOT NULL,
    config_value TEXT,
    config_type VARCHAR(16) DEFAULT 'STRING',
    remark VARCHAR(512),
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP(3),
    CONSTRAINT uk_sys_site_config UNIQUE (site_code, config_key)
);

CREATE TABLE IF NOT EXISTS sys_sensitive_word (
    word_id BIGINT PRIMARY KEY,
    word VARCHAR(64) NOT NULL,
    category VARCHAR(32) DEFAULT 'FORBIDDEN',
    level SMALLINT DEFAULT 1,
    replacement VARCHAR(64),
    status SMALLINT DEFAULT 1,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP(3),
    CONSTRAINT uk_sys_sensitive_word UNIQUE (word, category)
);

CREATE TABLE IF NOT EXISTS sys_message_template (
    template_id BIGINT PRIMARY KEY,
    template_code VARCHAR(64) NOT NULL,
    channel VARCHAR(32) NOT NULL,
    title VARCHAR(128),
    content TEXT NOT NULL,
    variables TEXT,
    status SMALLINT DEFAULT 1,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP(3),
    CONSTRAINT uk_sys_message_template UNIQUE (template_code, channel)
);

CREATE TABLE IF NOT EXISTS sys_export_task (
    task_id BIGINT PRIMARY KEY,
    module_name VARCHAR(64) NOT NULL,
    export_type VARCHAR(32) NOT NULL,
    query_params TEXT,
    status VARCHAR(16) DEFAULT 'PENDING',
    file_id BIGINT,
    file_size BIGINT,
    error_msg TEXT,
    create_by BIGINT,
    create_time TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    finish_time TIMESTAMP(3)
);
