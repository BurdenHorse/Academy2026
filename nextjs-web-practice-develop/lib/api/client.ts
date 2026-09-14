import axios from 'axios';

/** Địa chỉ base URL của API server lấy từ biến môi trường hoặc mặc định localhost:8085 */
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8085';

/** Instance axios dùng chung cho toàn bộ ứng dụng */
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

/**
 * Cấu hình interceptors cho Axios client:
 * 1. Request Interceptor: Tự động đính kèm Bearer token từ sessionStorage vào Authorization header.
 * 2. Response Interceptor: Bắt lỗi HTTP 401 Unauthorized để xóa token và chuyển hướng về trang /login.
 *
 * @param client Instance axios cần cấu hình interceptors
 */
export function setupInterceptors(client: ReturnType<typeof axios.create>): void {
  client.interceptors.request.use(
    (config) => {
      const token = sessionStorage.getItem('access_token');
      if (token) {
        if (config.headers) {
          config.headers.Authorization = `Bearer ${token}`;
        }
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  client.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 401) {
        sessionStorage.removeItem('access_token');
        sessionStorage.removeItem('token_type');
        if (typeof window !== 'undefined') {
          window.location.href = '/login';
        }
      }
      return Promise.reject(error);
    }
  );
}

setupInterceptors(apiClient);

export { apiClient };
