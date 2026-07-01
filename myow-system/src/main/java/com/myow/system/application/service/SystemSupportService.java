package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.ResultCode;
import com.myow.common.response.PageResult;
import com.myow.system.application.dto.SystemModels.ExportTaskCreateCommand;
import com.myow.system.application.dto.SystemModels.IdCommand;
import com.myow.system.application.dto.SystemModels.JobCreateCommand;
import com.myow.system.application.dto.SystemModels.JobUpdateCommand;
import com.myow.system.application.dto.SystemModels.MessageTemplateCreateCommand;
import com.myow.system.application.dto.SystemModels.MessageTemplatePreviewCommand;
import com.myow.system.application.dto.SystemModels.MessageTemplateUpdateCommand;
import com.myow.system.application.dto.SystemModels.NoticeCreateCommand;
import com.myow.system.application.dto.SystemModels.NoticeUpdateCommand;
import com.myow.system.application.dto.SystemModels.OnlineUserPageQuery;
import com.myow.system.application.dto.SystemModels.PageQuery;
import com.myow.system.application.dto.SystemModels.SensitiveWordCheckCommand;
import com.myow.system.application.dto.SystemModels.SensitiveWordCreateCommand;
import com.myow.system.application.dto.SystemModels.SensitiveWordUpdateCommand;
import com.myow.system.application.dto.SystemModels.SiteCodeQuery;
import com.myow.system.application.dto.SystemModels.SiteConfigCreateCommand;
import com.myow.system.application.dto.SystemModels.SiteConfigUpdateCommand;
import com.myow.system.application.vo.SystemRecordVO;
import com.myow.system.infrastructure.persistence.po.ExportTaskDO;
import com.myow.system.infrastructure.persistence.po.FileDO;
import com.myow.system.infrastructure.persistence.po.JobDO;
import com.myow.system.infrastructure.persistence.po.JobLogDO;
import com.myow.system.infrastructure.persistence.po.MessageTemplateDO;
import com.myow.system.infrastructure.persistence.po.NoticeDO;
import com.myow.system.infrastructure.persistence.po.SensitiveWordDO;
import com.myow.system.infrastructure.persistence.po.SiteConfigDO;
import com.myow.system.infrastructure.persistence.repository.ExportTaskRepository;
import com.myow.system.infrastructure.persistence.repository.FileRepository;
import com.myow.system.infrastructure.persistence.repository.JobLogRepository;
import com.myow.system.infrastructure.persistence.repository.JobRepository;
import com.myow.system.infrastructure.persistence.repository.MessageTemplateRepository;
import com.myow.system.infrastructure.persistence.repository.NoticeRepository;
import com.myow.system.infrastructure.persistence.repository.SensitiveWordRepository;
import com.myow.system.infrastructure.persistence.repository.SiteConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class SystemSupportService {

    private static final String LOCAL_STORAGE = "LOCAL";
    private static final long MAX_UPLOAD_SIZE = 50L * 1024L * 1024L;
    private static final Set<String> ALLOWED_FILE_SUFFIXES = Set.of("jpg", "jpeg", "png", "gif", "pdf", "doc", "docx", "xls", "xlsx", "csv", "txt", "zip", "rar");

    private final JobRepository jobRepository;
    private final JobLogRepository jobLogRepository;
    private final NoticeRepository noticeRepository;
    private final FileRepository fileRepository;
    private final SiteConfigRepository siteConfigRepository;
    private final SensitiveWordRepository sensitiveWordRepository;
    private final MessageTemplateRepository messageTemplateRepository;
    private final ExportTaskRepository exportTaskRepository;
    private final SystemFileStorageService fileStorageService;
    private final SystemJobExecutor jobExecutor;
    private final SystemExportTaskRunner exportTaskRunner;

    public SystemSupportService(JobRepository jobRepository,
                                JobLogRepository jobLogRepository,
                                NoticeRepository noticeRepository,
                                FileRepository fileRepository,
                                SiteConfigRepository siteConfigRepository,
                                SensitiveWordRepository sensitiveWordRepository,
                                MessageTemplateRepository messageTemplateRepository,
                                ExportTaskRepository exportTaskRepository,
                                SystemFileStorageService fileStorageService,
                                SystemJobExecutor jobExecutor,
                                SystemExportTaskRunner exportTaskRunner) {
        this.jobRepository = jobRepository;
        this.jobLogRepository = jobLogRepository;
        this.noticeRepository = noticeRepository;
        this.fileRepository = fileRepository;
        this.siteConfigRepository = siteConfigRepository;
        this.sensitiveWordRepository = sensitiveWordRepository;
        this.messageTemplateRepository = messageTemplateRepository;
        this.exportTaskRepository = exportTaskRepository;
        this.fileStorageService = fileStorageService;
        this.jobExecutor = jobExecutor;
        this.exportTaskRunner = exportTaskRunner;
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemRecordVO createJob(JobCreateCommand command) {
        validateText(command.jobName(), "jobName is required");
        validateText(command.jobGroup(), "jobGroup is required");
        validateText(command.cronExpression(), "cronExpression is required");
        validateText(command.handlerName(), "handlerName is required");
        if (jobRepository.lambdaQuery()
                .eq(JobDO::getJobName, command.jobName())
                .eq(JobDO::getJobGroup, command.jobGroup())
                .eq(JobDO::getDeletedFlag, false)
                .exists()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "job name already exists in group");
        }
        JobDO data = new JobDO()
                .setJobName(command.jobName())
                .setJobGroup(command.jobGroup())
                .setCronExpression(command.cronExpression())
                .setHandlerName(command.handlerName())
                .setExecutePolicy("SKIP")
                .setConcurrent(true)
                .setStatus(1)
                .setCreateTime(LocalDateTime.now())
                .setDeletedFlag(false);
        jobRepository.save(data);
        return toRecord(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemRecordVO updateJob(JobUpdateCommand command) {
        validateId(command.id());
        validateText(command.jobName(), "jobName is required");
        validateText(command.jobGroup(), "jobGroup is required");
        validateText(command.cronExpression(), "cronExpression is required");
        JobDO data = jobRepository.getById(command.id());
        if (data == null) {
            return null;
        }
        if (jobRepository.lambdaQuery()
                .eq(JobDO::getJobName, command.jobName())
                .eq(JobDO::getJobGroup, command.jobGroup())
                .eq(JobDO::getDeletedFlag, false)
                .ne(JobDO::getJobId, command.id())
                .exists()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "job name already exists in group");
        }
        data.setJobName(command.jobName())
                .setJobGroup(command.jobGroup())
                .setCronExpression(command.cronExpression())
                .setHandlerName(command.handlerName())
                .setStatus(command.status())
                .setUpdateTime(LocalDateTime.now());
        jobRepository.updateById(data);
        return toRecord(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemRecordVO createNotice(NoticeCreateCommand command) {
        validateText(command.title(), "title is required");
        validateText(command.content(), "content is required");
        NoticeDO data = new NoticeDO()
                .setTitle(command.title())
                .setContent(command.content())
                .setNoticeType(command.noticeType())
                .setExpireTime(command.expireTime())
                .setStatus(0)
                .setCreateTime(LocalDateTime.now())
                .setDeletedFlag(false);
        noticeRepository.save(data);
        return toRecord(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemRecordVO updateNotice(NoticeUpdateCommand command) {
        validateId(command.id());
        NoticeDO data = noticeRepository.getById(command.id());
        if (data == null) {
            return null;
        }
        if (!Objects.equals(data.getStatus(), 0)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "only draft notice can be updated");
        }
        data.setTitle(command.title())
                .setContent(command.content())
                .setNoticeType(command.noticeType())
                .setExpireTime(command.expireTime())
                .setUpdateTime(LocalDateTime.now());
        noticeRepository.updateById(data);
        return toRecord(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemRecordVO createSiteConfig(SiteConfigCreateCommand command) {
        validateText(command.siteCode(), "siteCode is required");
        validateText(command.configKey(), "configKey is required");
        if (siteConfigRepository.lambdaQuery()
                .eq(SiteConfigDO::getSiteCode, command.siteCode())
                .eq(SiteConfigDO::getConfigKey, command.configKey())
                .exists()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "site config already exists");
        }
        SiteConfigDO data = new SiteConfigDO()
                .setSiteCode(command.siteCode())
                .setConfigKey(command.configKey())
                .setConfigValue(command.configValue())
                .setConfigType(command.configType())
                .setRemark(command.remark())
                .setCreateTime(LocalDateTime.now());
        siteConfigRepository.save(data);
        return toRecord(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemRecordVO updateSiteConfig(SiteConfigUpdateCommand command) {
        validateId(command.id());
        validateText(command.siteCode(), "siteCode is required");
        validateText(command.configKey(), "configKey is required");
        SiteConfigDO data = siteConfigRepository.getById(command.id());
        if (data == null) {
            return null;
        }
        if (siteConfigRepository.lambdaQuery()
                .eq(SiteConfigDO::getSiteCode, command.siteCode())
                .eq(SiteConfigDO::getConfigKey, command.configKey())
                .ne(SiteConfigDO::getConfigId, command.id())
                .exists()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "site config already exists");
        }
        data.setSiteCode(command.siteCode())
                .setConfigKey(command.configKey())
                .setConfigValue(command.configValue())
                .setConfigType(command.configType())
                .setRemark(command.remark())
                .setUpdateTime(LocalDateTime.now());
        siteConfigRepository.updateById(data);
        return toRecord(data);
    }

    public Map<String, Object> getSiteConfigBySite(SiteCodeQuery query) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (SiteConfigDO data : siteConfigRepository.listBySiteCode(query.siteCode())) {
            result.put(data.getConfigKey(), data.getConfigValue());
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemRecordVO createSensitiveWord(SensitiveWordCreateCommand command) {
        validateText(command.word(), "word is required");
        validateText(command.category(), "category is required");
        if (sensitiveWordRepository.lambdaQuery()
                .eq(SensitiveWordDO::getWord, command.word())
                .eq(SensitiveWordDO::getCategory, command.category())
                .exists()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "sensitive word already exists");
        }
        SensitiveWordDO data = new SensitiveWordDO()
                .setWord(command.word())
                .setCategory(command.category())
                .setLevel(command.level())
                .setReplacement(command.replacement())
                .setStatus(1)
                .setCreateTime(LocalDateTime.now());
        sensitiveWordRepository.save(data);
        return toRecord(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemRecordVO updateSensitiveWord(SensitiveWordUpdateCommand command) {
        validateId(command.id());
        validateText(command.word(), "word is required");
        validateText(command.category(), "category is required");
        SensitiveWordDO data = sensitiveWordRepository.getById(command.id());
        if (data == null) {
            return null;
        }
        if (sensitiveWordRepository.lambdaQuery()
                .eq(SensitiveWordDO::getWord, command.word())
                .eq(SensitiveWordDO::getCategory, command.category())
                .ne(SensitiveWordDO::getWordId, command.id())
                .exists()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "sensitive word already exists");
        }
        data.setWord(command.word())
                .setCategory(command.category())
                .setLevel(command.level())
                .setReplacement(command.replacement())
                .setStatus(command.status())
                .setUpdateTime(LocalDateTime.now());
        sensitiveWordRepository.updateById(data);
        return toRecord(data);
    }

    public Map<String, Object> checkSensitiveWord(SensitiveWordCheckCommand command) {
        List<String> hits = sensitiveWordRepository.list().stream()
                .filter(word -> Objects.equals(word.getStatus(), 1))
                .map(SensitiveWordDO::getWord)
                .filter(word -> command.text() != null && command.text().contains(word))
                .toList();
        String replaced = command.text();
        for (String hit : hits) {
            replaced = replaced.replace(hit, "***");
        }
        return attrs("hits", hits, "replacedText", replaced);
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemRecordVO createMessageTemplate(MessageTemplateCreateCommand command) {
        validateText(command.templateCode(), "templateCode is required");
        validateText(command.channel(), "channel is required");
        validateText(command.content(), "content is required");
        if (messageTemplateRepository.lambdaQuery()
                .eq(MessageTemplateDO::getTemplateCode, command.templateCode())
                .eq(MessageTemplateDO::getChannel, command.channel())
                .exists()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "message template already exists");
        }
        MessageTemplateDO data = new MessageTemplateDO()
                .setTemplateCode(command.templateCode())
                .setChannel(command.channel())
                .setTitle(command.title())
                .setContent(command.content())
                .setVariables(command.variables())
                .setStatus(1)
                .setCreateTime(LocalDateTime.now());
        messageTemplateRepository.save(data);
        return toRecord(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemRecordVO updateMessageTemplate(MessageTemplateUpdateCommand command) {
        validateId(command.id());
        validateText(command.templateCode(), "templateCode is required");
        validateText(command.channel(), "channel is required");
        MessageTemplateDO data = messageTemplateRepository.getById(command.id());
        if (data == null) {
            return null;
        }
        if (messageTemplateRepository.lambdaQuery()
                .eq(MessageTemplateDO::getTemplateCode, command.templateCode())
                .eq(MessageTemplateDO::getChannel, command.channel())
                .ne(MessageTemplateDO::getTemplateId, command.id())
                .exists()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "message template already exists");
        }
        data.setTemplateCode(command.templateCode())
                .setChannel(command.channel())
                .setTitle(command.title())
                .setContent(command.content())
                .setVariables(command.variables())
                .setStatus(command.status())
                .setUpdateTime(LocalDateTime.now());
        messageTemplateRepository.updateById(data);
        return toRecord(data);
    }

    public Map<String, Object> previewMessageTemplate(MessageTemplatePreviewCommand command) {
        MessageTemplateDO template = command.id() == null ? null : messageTemplateRepository.getById(command.id());
        String content = template == null ? "" : template.getContent();
        if (command.variables() != null) {
            for (Map.Entry<String, String> entry : command.variables().entrySet()) {
                content = content.replace("${" + entry.getKey() + "}", entry.getValue());
            }
        }
        return attrs("content", content);
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemRecordVO createExportTask(ExportTaskCreateCommand command) {
        validateText(command.moduleName(), "moduleName is required");
        validateText(command.exportType(), "exportType is required");
        ExportTaskDO data = new ExportTaskDO()
                .setModuleName(command.moduleName())
                .setExportType(command.exportType())
                .setQueryParams(command.queryParams() == null ? null : command.queryParams().toString())
                .setStatus("PENDING")
                .setCreateTime(LocalDateTime.now());
        exportTaskRepository.save(data);
        exportTaskRunner.submit(data.getTaskId());
        return toRecord(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemRecordVO uploadFile(MultipartFile file, String moduleName) {
        validateText(moduleName, "moduleName is required");
        validateFile(file);
        String originalName = file == null ? "" : file.getOriginalFilename();
        SystemFileStorageService.StoredFile storedFile = fileStorageService.store(file, moduleName);
        FileDO data = new FileDO()
                .setModuleName(moduleName)
                .setOriginalName(originalName)
                .setStorageType(LOCAL_STORAGE)
                .setStorageKey(storedFile.storageKey())
                .setFileSize(file == null ? 0L : file.getSize())
                .setContentType(file == null ? null : file.getContentType())
                .setCreateTime(LocalDateTime.now())
                .setDeletedFlag(false);
        fileRepository.save(data);
        return toRecord(data);
    }

    public List<SystemRecordVO> batchUploadFile(MultipartFile[] files, String moduleName) {
        if (files == null) {
            return List.of();
        }
        return java.util.Arrays.stream(files).map(file -> uploadFile(file, moduleName)).toList();
    }

    public SystemRecordVO detail(String type, IdCommand command) {
        validateId(command.id());
        return switch (type) {
            case "JOB" -> toRecord(jobRepository.getById(command.id()));
            case "NOTICE" -> toRecord(noticeRepository.getById(command.id()));
            case "FILE" -> toRecord(fileRepository.getById(command.id()));
            case "SITE_CONFIG" -> toRecord(siteConfigRepository.getById(command.id()));
            case "SENSITIVE_WORD" -> toRecord(sensitiveWordRepository.getById(command.id()));
            case "MESSAGE_TEMPLATE" -> toRecord(messageTemplateRepository.getById(command.id()));
            case "EXPORT_TASK" -> toRecord(exportTaskRepository.getById(command.id()));
            default -> null;
        };
    }

    public PageResult<SystemRecordVO> page(String type, PageQuery query) {
        long pageNum = normalizePageNum(query == null ? null : query.pageNum());
        long pageSize = normalizePageSize(query == null ? null : query.pageSize());
        String keyword = query == null ? null : query.keyword();
        Integer status = query == null ? null : query.status();
        return switch (type) {
            case "JOB" -> convert(jobRepository.selectPage(keyword, status, pageNum, pageSize), this::toRecord);
            case "JOB_LOG" -> convert(jobLogRepository.selectPage(pageNum, pageSize), this::toRecord);
            case "NOTICE" -> convert(noticeRepository.selectPage(keyword, status, pageNum, pageSize), this::toRecord);
            case "FILE" -> convert(fileRepository.selectPage(keyword, pageNum, pageSize), this::toRecord);
            case "SITE_CONFIG" -> convert(siteConfigRepository.selectPage(keyword, pageNum, pageSize), this::toRecord);
            case "SENSITIVE_WORD" -> convert(sensitiveWordRepository.selectPage(keyword, status, pageNum, pageSize), this::toRecord);
            case "MESSAGE_TEMPLATE" -> convert(messageTemplateRepository.selectPage(keyword, status, pageNum, pageSize), this::toRecord);
            case "EXPORT_TASK" -> convert(exportTaskRepository.selectPage(keyword, pageNum, pageSize), this::toRecord);
            default -> PageResult.empty();
        };
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(String type, IdCommand command) {
        validateId(command.id());
        return switch (type) {
            case "JOB" -> softDeleteJob(command.id());
            case "NOTICE" -> softDeleteNotice(command.id());
            case "FILE" -> softDeleteFile(command.id());
            case "SITE_CONFIG" -> siteConfigRepository.removeById(command.id());
            case "SENSITIVE_WORD" -> sensitiveWordRepository.removeById(command.id());
            case "MESSAGE_TEMPLATE" -> messageTemplateRepository.removeById(command.id());
            case "EXPORT_TASK" -> exportTaskRepository.removeById(command.id());
            default -> false;
        };
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemRecordVO changeStatus(String type, IdCommand command, int status) {
        validateId(command.id());
        if ("JOB".equals(type)) {
            JobDO data = jobRepository.getById(command.id());
            if (data == null) {
                return null;
            }
            if (status == 1) {
                jobExecutor.executeOnce(data);
            }
            data.setStatus(status).setUpdateTime(LocalDateTime.now());
            jobRepository.updateById(data);
            return toRecord(data);
        }
        if ("NOTICE".equals(type)) {
            NoticeDO data = noticeRepository.getById(command.id());
            if (data == null) {
                return null;
            }
            data.setStatus(status).setUpdateTime(LocalDateTime.now());
            if (status == 1) {
                data.setPublishTime(LocalDateTime.now());
            }
            noticeRepository.updateById(data);
            return toRecord(data);
        }
        return null;
    }

    public Map<String, Object> serverMetrics() {
        Runtime runtime = Runtime.getRuntime();
        return attrs("availableProcessors", runtime.availableProcessors(), "freeMemory", runtime.freeMemory(),
                "totalMemory", runtime.totalMemory(), "maxMemory", runtime.maxMemory());
    }

    public Map<String, Object> redisMetrics() {
        return attrs("status", "UNKNOWN", "message", "Redis metrics adapter is not connected yet");
    }

    public Map<String, Object> dbMetrics() {
        return attrs("status", "UNKNOWN", "message", "Database metrics adapter is not connected yet");
    }

    public PageResult<SystemRecordVO> onlineUsers(OnlineUserPageQuery query) {
        return PageResult.empty();
    }

    public Boolean kickOnlineUser(String token) {
        return token != null && !token.isBlank();
    }

    private Boolean softDeleteJob(Long id) {
        JobDO data = jobRepository.getById(id);
        if (data == null) {
            return false;
        }
        data.setDeletedFlag(true).setUpdateTime(LocalDateTime.now());
        return jobRepository.updateById(data);
    }

    private Boolean softDeleteNotice(Long id) {
        NoticeDO data = noticeRepository.getById(id);
        if (data == null) {
            return false;
        }
        data.setDeletedFlag(true).setUpdateTime(LocalDateTime.now());
        return noticeRepository.updateById(data);
    }

    private Boolean softDeleteFile(Long id) {
        FileDO data = fileRepository.getById(id);
        if (data == null) {
            return false;
        }
        data.setDeletedFlag(true);
        return fileRepository.updateById(data);
    }

    private SystemRecordVO toRecord(JobDO data) {
        if (data == null) {
            return null;
        }
        return new SystemRecordVO(data.getJobId(), "JOB", data.getJobName(), data.getJobGroup(), data.getStatus(),
                attrs("cronExpression", data.getCronExpression(), "handlerName", data.getHandlerName(), "executePolicy", data.getExecutePolicy()),
                data.getCreateTime(), data.getUpdateTime());
    }

    private SystemRecordVO toRecord(JobLogDO data) {
        if (data == null) {
            return null;
        }
        return new SystemRecordVO(data.getLogId(), "JOB_LOG", data.getJobName(), data.getJobGroup(), "SUCCESS".equals(data.getStatus()) ? 1 : 0,
                attrs("jobId", data.getJobId(), "startTime", data.getStartTime(), "endTime", data.getEndTime(), "errorMsg", data.getErrorMsg()),
                data.getStartTime(), data.getEndTime());
    }

    private SystemRecordVO toRecord(NoticeDO data) {
        if (data == null) {
            return null;
        }
        return new SystemRecordVO(data.getNoticeId(), "NOTICE", data.getTitle(), data.getTitle(), data.getStatus(),
                attrs("content", data.getContent(), "noticeType", data.getNoticeType(), "publishTime", data.getPublishTime(), "expireTime", data.getExpireTime()),
                data.getCreateTime(), data.getUpdateTime());
    }

    private SystemRecordVO toRecord(FileDO data) {
        if (data == null) {
            return null;
        }
        return new SystemRecordVO(data.getFileId(), "FILE", data.getOriginalName(), data.getModuleName(), Boolean.TRUE.equals(data.getDeletedFlag()) ? 0 : 1,
                attrs("storageType", data.getStorageType(), "storageKey", data.getStorageKey(), "fileSize", data.getFileSize(), "contentType", data.getContentType()),
                data.getCreateTime(), null);
    }

    private SystemRecordVO toRecord(SiteConfigDO data) {
        if (data == null) {
            return null;
        }
        return new SystemRecordVO(data.getConfigId(), "SITE_CONFIG", data.getConfigKey(), data.getSiteCode(), 1,
                attrs("configValue", data.getConfigValue(), "configType", data.getConfigType(), "remark", data.getRemark()),
                data.getCreateTime(), data.getUpdateTime());
    }

    private SystemRecordVO toRecord(SensitiveWordDO data) {
        if (data == null) {
            return null;
        }
        return new SystemRecordVO(data.getWordId(), "SENSITIVE_WORD", data.getWord(), data.getCategory(), data.getStatus(),
                attrs("level", data.getLevel(), "replacement", data.getReplacement()), data.getCreateTime(), data.getUpdateTime());
    }

    private SystemRecordVO toRecord(MessageTemplateDO data) {
        if (data == null) {
            return null;
        }
        return new SystemRecordVO(data.getTemplateId(), "MESSAGE_TEMPLATE", data.getTemplateCode(), data.getTitle(), data.getStatus(),
                attrs("channel", data.getChannel(), "content", data.getContent(), "variables", data.getVariables()), data.getCreateTime(), data.getUpdateTime());
    }

    private SystemRecordVO toRecord(ExportTaskDO data) {
        if (data == null) {
            return null;
        }
        return new SystemRecordVO(data.getTaskId(), "EXPORT_TASK", data.getModuleName(), data.getExportType(), "SUCCESS".equals(data.getStatus()) ? 1 : 0,
                attrs("queryParams", data.getQueryParams(), "status", data.getStatus(), "fileId", data.getFileId(), "errorMsg", data.getErrorMsg()),
                data.getCreateTime(), data.getFinishTime());
    }

    private <T> PageResult<SystemRecordVO> convert(Page<T> page, java.util.function.Function<T, SystemRecordVO> mapper) {
        PageResult<SystemRecordVO> result = new PageResult<>();
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setList(page.getRecords().stream().map(mapper).toList());
        result.setEmptyFlag(page.getRecords().isEmpty());
        return result;
    }

    private static long normalizePageNum(Long pageNum) {
        return pageNum == null || pageNum < 1 ? 1 : pageNum;
    }

    private static long normalizePageSize(Long pageSize) {
        return pageSize == null || pageSize < 1 ? 20 : pageSize;
    }

    private static void validateId(Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "id is required");
        }
    }

    private static void validateText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, message);
        }
    }

    private static void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "file is required");
        }
        if (file.getSize() > MAX_UPLOAD_SIZE) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "file size exceeds 50MB");
        }
        String originalName = file.getOriginalFilename();
        if (!StringUtils.hasText(originalName) || !originalName.contains(".")) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "file suffix is required");
        }
        String suffix = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_FILE_SUFFIXES.contains(suffix)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "file type is not allowed");
        }
    }

    private static Map<String, Object> attrs(Object... values) {
        Map<String, Object> attributes = new LinkedHashMap<>();
        for (int i = 0; i + 1 < values.length; i += 2) {
            attributes.put(String.valueOf(values[i]), values[i + 1]);
        }
        return attributes;
    }
}
