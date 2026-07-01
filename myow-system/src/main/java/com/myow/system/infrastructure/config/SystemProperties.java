package com.myow.system.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "myow.system")
public class SystemProperties {

    private String uploadRoot = ".uploads/system";
    private int exportPoolSize = 2;

    public String getUploadRoot() {
        return uploadRoot;
    }

    public void setUploadRoot(String uploadRoot) {
        this.uploadRoot = uploadRoot;
    }

    public int getExportPoolSize() {
        return exportPoolSize;
    }

    public void setExportPoolSize(int exportPoolSize) {
        this.exportPoolSize = exportPoolSize;
    }
}
