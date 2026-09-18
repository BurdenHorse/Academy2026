import { apiClient } from './client';
import { ListCertificationResponse } from '@/types/certification';
import { API_ENDPOINTS } from '@/constants';

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
    const response = await apiClient.get<ListCertificationResponse>(API_ENDPOINTS.CERTIFICATIONS);
    return response.data;
  },
};
