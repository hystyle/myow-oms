INSERT INTO sys_dict(dict_id, dict_name, dict_code, remark, create_time, update_time)
VALUES
    (210030, '通用是否', 'sys_yes_no', 'true=是, false=否', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3))
ON CONFLICT (dict_code) DO NOTHING;

INSERT INTO sys_dict_data(dict_data_id, dict_id, data_value, data_label, sort, disabled_flag, create_time, update_time)
VALUES
    (211099, 210030, 'true', '是', 1, false, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3)),
    (211100, 210030, 'false', '否', 2, false, CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3))
ON CONFLICT (dict_data_id) DO NOTHING;
