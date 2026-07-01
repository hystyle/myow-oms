-- Default system maintenance jobs.

INSERT INTO sys_job(job_id, job_name, job_group, cron_expression, handler_name, execute_policy, concurrent, status, remark, create_time, update_time, deleted_flag)
VALUES
    (90001, 'Cleanup Expired Export Files', 'SYSTEM', '0 0 2 * * ?', 'systemMaintenanceJobs.cleanupExpiredExportFiles', 'SKIP', false, 0, 'Clean expired export files every day at 02:00.', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (90002, 'Cleanup Expired File Records', 'SYSTEM', '0 0 3 * * ?', 'systemMaintenanceJobs.cleanupExpiredFileRecords', 'SKIP', false, 0, 'Clean expired file metadata every day at 03:00.', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false),
    (90003, 'Scan Expired Notices', 'SYSTEM', '0 0 * * * ?', 'systemMaintenanceJobs.scanExpiredNotices', 'SKIP', false, 0, 'Scan expired notices every hour.', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3), false)
ON CONFLICT (job_id) DO UPDATE SET
    job_name = EXCLUDED.job_name,
    job_group = EXCLUDED.job_group,
    cron_expression = EXCLUDED.cron_expression,
    handler_name = EXCLUDED.handler_name,
    execute_policy = EXCLUDED.execute_policy,
    concurrent = EXCLUDED.concurrent,
    status = EXCLUDED.status,
    remark = EXCLUDED.remark,
    deleted_flag = false,
    update_time = CURRENT_TIMESTAMP(3);
