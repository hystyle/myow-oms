package com.myow.system.application.vo;

import org.springframework.core.io.Resource;

public record SystemDownloadFile(String fileName, String contentType, Resource resource) {
}
