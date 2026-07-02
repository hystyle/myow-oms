import { request } from '@myow/shared';
import type { BootstrapPayload, LoginPayload, LoginResult } from '@myow/api';

export function login(payload: LoginPayload) {
  return request.post<LoginResult>('/myow/auth/login', payload);
}

export function logout() {
  return request.post<boolean>('/myow/auth/logout', {});
}

export function bootstrap() {
  return request.post<BootstrapPayload>('/myow/profile/bootstrap', {});
}
