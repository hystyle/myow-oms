package com.myow.system.application.service;

import org.springframework.stereotype.Component;

@Component("systemMaintenanceJobs")
public class SystemMaintenanceJobs {

    public void cleanupExpiredExportFiles() {
        // Placeholder for scheduled cleanup. The job log still records execution.
    }

    public void cleanupExpiredFileRecords() {
        // Placeholder for scheduled cleanup. The job log still records execution.
    }

    public void scanExpiredNotices() {
        // Placeholder for scheduled notice expiry. The job log still records execution.
    }
}
