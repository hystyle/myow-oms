import { request } from '@myow/shared';
import type { ApiId, PageQuery, PageResult, SystemRecord } from '@myow/api';

const JOB_BASE = '/myow/api/v1/system/jobs';
const NOTICE_BASE = '/myow/api/v1/system/notices';

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

export function pageNotices(query: PageQuery) {
  return request.post<PageResult<SystemRecord>>(`${NOTICE_BASE}/page`, query);
}

export function publishNotice(id: ApiId) {
  return request.post<SystemRecord>(`${NOTICE_BASE}/publish`, { id });
}

export function withdrawNotice(id: ApiId) {
  return request.post<SystemRecord>(`${NOTICE_BASE}/withdraw`, { id });
}
