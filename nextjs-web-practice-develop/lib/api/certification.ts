import { apiClient } from './client';
import { ListCertificationResponse } from '@/types/certification';

/**
 * Module cung cấp các hàm gọi API liên quan đến chứng chỉ tiếng Nhật.
 */
export const certificationApi = {
  /**
   * Lấy danh sách tất cả các chứng chỉ tiếng Nhật trong hệ thống.
   * Endpoint: GET /certifications
   *
   * @returns Promise<ListCertificationResponse> Chứa danh sách các chứng chỉ
   */
  getCertifications: async (): Promise<ListCertificationResponse> => {
    const response = await apiClient.get<ListCertificationResponse>('/certifications');
    return response.data;
  },
};
