import axios from 'axios';
import { getToken } from './token';

interface ApiResult<T> { code?: string | number; message?: string; data: T; }

export const requestClient = axios.create({ timeout: 15000 });

requestClient.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = token;
  }
  return config;
});

requestClient.interceptors.response.use((response) => {
  if (response.config.responseType === 'blob') {
    const disposition = response.headers?.['content-disposition'] ?? '';
    return {
      blob: response.data,
      fileName: parseFileName(disposition) || 'download'
    };
  }
  const body = response.data as ApiResult<unknown>;
  if (body && Object.prototype.hasOwnProperty.call(body, 'data')) {
    if (body.code && Number(body.code) !== 200) {
      throw new Error(body.message || 'Request failed');
    }
    return body.data;
  }
  return response.data;
});

export const request = {
  post<T>(url: string, data?: unknown) {
    return requestClient.post<unknown, T>(url, data);
  },
  download(url: string, data?: unknown) {
    return requestClient.post<unknown, { blob: Blob; fileName: string }>(url, data, { responseType: 'blob' });
  }
};

function parseFileName(disposition: string) {
  const encoded = /filename\*=UTF-8''([^;]+)/i.exec(disposition)?.[1];
  if (encoded) {
    return decodeURIComponent(encoded);
  }
  return /filename="?([^"]+)"?/i.exec(disposition)?.[1] ?? '';
}
