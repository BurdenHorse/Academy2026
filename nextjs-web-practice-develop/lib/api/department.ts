import { apiClient } from './client';
import { ListDepartmentResponse } from '@/types/department';

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
    const response = await apiClient.get<ListDepartmentResponse>('/departments');
    return response.data;
  },
};
