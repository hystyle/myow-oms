-- Align backend menus with the frontend pages available in the startup phase.
-- Future system pages stay seeded for permission design, but are hidden until
-- their frontend screens and controllers are ready.

UPDATE sys_menu
SET visible = '1',
    update_time = CURRENT_TIMESTAMP(3)
WHERE menu_id IN (
    1500, 1501, 1502, 1503,
    1600, 1601, 1602, 1603,
    1800, 1801, 1802,
    1810, 1811, 1812, 1813,
    1830, 1831, 1832, 1833,
    1840, 1841, 1842, 1843,
    1850, 1851, 1852, 1853,
    1860, 1861, 1862, 1863,
    1870
);

UPDATE sys_menu
SET visible = '0',
    update_time = CURRENT_TIMESTAMP(3)
WHERE menu_id IN (
    1000, 1100, 1101, 1102, 1103,
    1200, 1300, 1400, 1700, 1820,
    1900, 1910, 1911, 1912, 1913, 1914, 1915, 1916,
    1920, 1921, 1922, 1923, 1924, 1925,
    1930, 1931, 1932, 1933,
    1940, 1941, 1942, 1943, 1944,
    1950, 1951, 1952, 1953, 1954,
    1960, 1961, 1962, 1963, 1964,
    1970, 1971, 1972, 1973,
    1980, 1990, 1991
);

INSERT INTO sys_site_config(config_id, site_code, config_key, config_value, config_type, remark, create_time, update_time)
VALUES
    (91001, 'ADMIN', 'ui.theme', 'workbench', 'STRING', 'Default admin theme for startup phase.', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3)),
    (91002, 'ADMIN', 'security.login.rate-limit', '60/min', 'STRING', 'Login rate limit display value.', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3)),
    (91003, 'CLIENT', 'portal.brand-name', 'MYOW Platform', 'STRING', 'Client portal brand name.', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3))
ON CONFLICT (site_code, config_key) DO UPDATE SET
    config_value = EXCLUDED.config_value,
    config_type = EXCLUDED.config_type,
    remark = EXCLUDED.remark,
    update_time = CURRENT_TIMESTAMP(3);

INSERT INTO sys_notice(notice_id, title, content, notice_type, status, publish_time, expire_time, create_time, update_time, deleted_flag)
VALUES
    (92001, 'System Module Trial Ready', 'System management pages are available for startup verification.', 'SYSTEM', 1, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3) + INTERVAL '30 days', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (92002, 'Customer Portal Preview', 'Client portal pages are in preview mode and will be connected to business APIs later.', 'PORTAL', 0, null, CURRENT_TIMESTAMP(3) + INTERVAL '30 days', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false)
ON CONFLICT (notice_id) DO UPDATE SET
    title = EXCLUDED.title,
    content = EXCLUDED.content,
    notice_type = EXCLUDED.notice_type,
    status = EXCLUDED.status,
    publish_time = EXCLUDED.publish_time,
    expire_time = EXCLUDED.expire_time,
    deleted_flag = false,
    update_time = CURRENT_TIMESTAMP(3);

INSERT INTO sys_sensitive_word(word_id, word, category, level, replacement, status, create_time, update_time)
VALUES
    (93001, 'forbidden-test', 'COMPLIANCE', 1, '***', 1, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3)),
    (93002, 'blocked-demo', 'COMPLIANCE', 2, '***', 1, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3))
ON CONFLICT (word, category) DO UPDATE SET
    level = EXCLUDED.level,
    replacement = EXCLUDED.replacement,
    status = EXCLUDED.status,
    update_time = CURRENT_TIMESTAMP(3);

INSERT INTO sys_message_template(template_id, template_code, channel, title, content, variables, status, create_time, update_time)
VALUES
    (94001, 'USER_PASSWORD_RESET', 'EMAIL',
     'Password reset for ' || CHR(36) || '{name}',
     'Hello ' || CHR(36) || '{name}, your temporary password is ' || CHR(36) || '{password}.',
     'name,password', 1, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3)),
    (94002, 'EXPORT_TASK_READY', 'SITE_MESSAGE',
     'Export task ' || CHR(36) || '{taskName} is ready',
     'Your export task ' || CHR(36) || '{taskName} has completed. File id: ' || CHR(36) || '{fileId}.',
     'taskName,fileId', 1, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3)),
    (94003, 'NOTICE_PUBLISH', 'SITE_MESSAGE',
     'Notice: ' || CHR(36) || '{title}',
     CHR(36) || '{content}',
     'title,content', 1, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3))
ON CONFLICT (template_code, channel) DO UPDATE SET
    title = EXCLUDED.title,
    content = EXCLUDED.content,
    variables = EXCLUDED.variables,
    status = EXCLUDED.status,
    update_time = CURRENT_TIMESTAMP(3);
