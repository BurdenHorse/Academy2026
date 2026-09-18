import { apiClient } from './client';
import { ListDepartmentResponse } from '@/types/department';
import { API_ENDPOINTS } from '@/constants';

/**
 * Module cung cấp các hàm gọi API liên quan đến phòng ban.
 */
export const departmentApi = {
  /**
   * Lấy danh sách tất cả các phòng ban trong hệ thống.
   * Endpoint: GET /departments
   *
   * @returns Promise<ListDepartmentResponse> Chứa danh sách các phòng ban
   */
  getDepartments: async (): Promise<ListDepartmentResponse> => {
    const response = await apiClient.get<ListDepartmentResponse>(API_ENDPOINTS.DEPARTMENTS);
    return response.data;
  },
};
