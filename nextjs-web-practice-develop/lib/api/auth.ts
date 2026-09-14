import { apiClient } from './client';
import { LoginRequest, LoginResponse } from '@/types/auth';

/**
 * Module cung cấp các hàm gọi API liên quan đến xác thực người dùng.
 */
export const authApi = {
  /**
   * Gọi API đăng nhập để lấy JWT access token.
   *
   * @param data Dữ liệu đăng nhập gồm username và password
   * @returns Promise chứa thông tin response (accessToken, tokenType hoặc lỗi)
   */
  login: async (data: LoginRequest): Promise<LoginResponse> => {
    const response = await apiClient.post<LoginResponse>('/login', data);
    return response.data;
  },
};
