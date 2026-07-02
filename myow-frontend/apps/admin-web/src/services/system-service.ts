import { request } from '@myow/shared';
import type { ApiId, PageQuery, PageResult, SystemRecord } from '@myow/api';

const JOB_BASE = '/myow/api/v1/system/jobs';
const NOTICE_BASE = '/myow/api/v1/system/notices';
const SITE_CONFIG_BASE = '/myow/api/v1/system/site-configs';
const FILE_BASE = '/myow/api/v1/system/files';
const ONLINE_USER_BASE = '/myow/api/v1/system/online-users';

export function createJob(data: Record<string, unknown>) {
  return request.post<SystemRecord>(`${JOB_BASE}/create`, data);
}

export function updateJob(data: Record<string, unknown>) {
  return request.post<SystemRecord>(`${JOB_BASE}/update`, data);
}

export function pageJobs(query: PageQuery) {
  return request.post<PageResult<SystemRecord>>(`${JOB_BASE}/page`, query);
}

export function pageJobLogs(query: PageQuery) {
  return request.post<PageResult<SystemRecord>>(`${JOB_BASE}/log-page`, query);
}

export function runJob(id: ApiId) {
  return request.post<SystemRecord>(`${JOB_BASE}/run`, { id });
}

export function pauseJob(id: ApiId) {
  return request.post<SystemRecord>(`${JOB_BASE}/pause`, { id });
}

export function resumeJob(id: ApiId) {
  return request.post<SystemRecord>(`${JOB_BASE}/resume`, { id });
}

export function deleteJob(id: ApiId) {
  return request.post<boolean>(`${JOB_BASE}/delete`, { id });
}

export function createNotice(data: Record<string, unknown>) {
  return request.post<SystemRecord>(`${NOTICE_BASE}/create`, data);
}

export function updateNotice(data: Record<string, unknown>) {
  return request.post<SystemRecord>(`${NOTICE_BASE}/update`, data);
}

export function pageNotices(query: PageQuery) {
  return request.post<PageResult<SystemRecord>>(`${NOTICE_BASE}/page`, query);
}

export function publishNotice(id: ApiId) {
  return request.post<SystemRecord>(`${NOTICE_BASE}/publish`, { id });
}

export function withdrawNotice(id: ApiId) {
  return request.post<SystemRecord>(`${NOTICE_BASE}/withdraw`, { id });
}

export function deleteNotice(id: ApiId) {
  return request.post<boolean>(`${NOTICE_BASE}/delete`, { id });
}

export function createSiteConfig(data: Record<string, unknown>) {
  return request.post<SystemRecord>(`${SITE_CONFIG_BASE}/create`, data);
}

export function updateSiteConfig(data: Record<string, unknown>) {
  return request.post<SystemRecord>(`${SITE_CONFIG_BASE}/update`, data);
}

export function pageSiteConfigs(query: PageQuery) {
  return request.post<PageResult<SystemRecord>>(`${SITE_CONFIG_BASE}/page`, query);
}

export function deleteSiteConfig(id: ApiId) {
  return request.post<boolean>(`${SITE_CONFIG_BASE}/delete`, { id });
}

export function refreshSiteConfig(siteCode: string) {
  return request.post<boolean>(`${SITE_CONFIG_BASE}/refresh`, { siteCode });
}

export function pageFiles(query: PageQuery) {
  return request.post<PageResult<SystemRecord>>(`${FILE_BASE}/page`, query);
}

export function uploadFile(file: File, moduleName: string) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('moduleName', moduleName);
  return request.post<SystemRecord>(`${FILE_BASE}/upload`, formData);
}

export function downloadFile(id: ApiId) {
  return request.download(`${FILE_BASE}/download`, { id });
}

export function deleteFile(id: ApiId) {
  return request.post<boolean>(`${FILE_BASE}/delete`, { id });
}

export function pageOnlineUsers(query: PageQuery) {
  return request.post<PageResult<SystemRecord>>(`${ONLINE_USER_BASE}/page`, query);
}
