package com.myow.system.application.service;

import com.myow.system.infrastructure.config.SystemProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class SystemFileStorageService {

    private final SystemProperties properties;

    public SystemFileStorageService(SystemProperties properties) {
        this.properties = properties;
    }

    public StoredFile store(MultipartFile file, String moduleName) {
        String originalName = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
        String suffix = originalName.contains(".") ? originalName.substring(originalName.lastIndexOf('.')) : "";
        String objectName = UUID.randomUUID() + suffix;
        LocalDate today = LocalDate.now();
        Path targetDir = Path.of(properties.getUploadRoot(), moduleName, String.valueOf(today.getYear()),
                String.format("%02d", today.getMonthValue()), String.format("%02d", today.getDayOfMonth()));
        try {
            Files.createDirectories(targetDir);
            Path target = targetDir.resolve(objectName);
            file.transferTo(target);
            return new StoredFile(target.toString().replace('\\', '/'), target.toAbsolutePath().toString());
        } catch (IOException ex) {
            throw new UncheckedIOException("failed to store file", ex);
        }
    }

    public record StoredFile(String storageKey, String absolutePath) {
    }
}
