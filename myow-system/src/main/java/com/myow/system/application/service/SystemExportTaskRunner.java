package com.myow.system.application.service;

import com.myow.system.infrastructure.config.SystemProperties;
import com.myow.system.infrastructure.persistence.po.ExportTaskDO;
import com.myow.system.infrastructure.persistence.po.FileDO;
import com.myow.system.infrastructure.persistence.repository.ExportTaskRepository;
import com.myow.system.infrastructure.persistence.repository.FileRepository;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SystemExportTaskRunner {

    private final ExportTaskRepository exportTaskRepository;
    private final FileRepository fileRepository;
    private final SystemProperties properties;
    private final ExecutorService executorService;

    public SystemExportTaskRunner(ExportTaskRepository exportTaskRepository,
                                  FileRepository fileRepository,
                                  SystemProperties properties) {
        this.exportTaskRepository = exportTaskRepository;
        this.fileRepository = fileRepository;
        this.properties = properties;
        this.executorService = Executors.newFixedThreadPool(Math.max(1, properties.getExportPoolSize()));
    }

    public void submit(Long taskId) {
        executorService.submit(() -> run(taskId));
    }

    private void run(Long taskId) {
        ExportTaskDO task = exportTaskRepository.getById(taskId);
        if (task == null) {
            return;
        }
        task.setStatus("RUNNING");
        exportTaskRepository.updateById(task);
        try {
            Path dir = Path.of(properties.getUploadRoot(), "export");
            Files.createDirectories(dir);
            Path target = dir.resolve("export-task-" + task.getTaskId() + ".json");
            String content = """
                    {
                      "taskId": "%s",
                      "moduleName": "%s",
                      "exportType": "%s",
                      "queryParams": "%s"
                    }
                    """.formatted(task.getTaskId(), task.getModuleName(), task.getExportType(), task.getQueryParams());
            Files.writeString(target, content, StandardCharsets.UTF_8);

            FileDO file = new FileDO()
                    .setModuleName("export")
                    .setOriginalName(target.getFileName().toString())
                    .setStorageType("LOCAL")
                    .setStorageKey(target.toString().replace('\\', '/'))
                    .setFileSize(Files.size(target))
                    .setContentType("application/json")
                    .setCreateTime(LocalDateTime.now())
                    .setDeletedFlag(false);
            fileRepository.save(file);

            task.setFileId(file.getFileId())
                    .setFileSize(file.getFileSize())
                    .setStatus("SUCCESS")
                    .setFinishTime(LocalDateTime.now());
            exportTaskRepository.updateById(task);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (Exception ex) {
            task.setStatus("FAILED")
                    .setErrorMsg(ex.getMessage())
                    .setFinishTime(LocalDateTime.now());
            exportTaskRepository.updateById(task);
        }
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }
}
