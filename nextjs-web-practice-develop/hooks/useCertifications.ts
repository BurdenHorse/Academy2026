import { useState, useEffect, useCallback } from 'react';
import { certificationApi } from '@/lib/api/certification';
import { CertificationDTO } from '@/types/certification';
import { MESSAGES } from '@/constants';

/**
 * Custom hook quản lý việc lấy danh sách chứng chỉ tiếng Nhật (Certifications).
 *
 * @returns Object chứa danh sách certifications, trạng thái loading, lỗi và hàm fetchCertifications
 */
export function useCertifications() {
  const [certifications, setCertifications] = useState<CertificationDTO[]>([]);
  const [isLoadingCertifications, setIsLoadingCertifications] = useState<boolean>(true);
  const [certificationError, setCertificationError] = useState<string | null>(null);

  /**
   * Gọi API lấy danh sách chứng chỉ tiếng Nhật từ server.
   */
  const fetchCertifications = useCallback(async () => {
    setIsLoadingCertifications(true);
    setCertificationError(null);
    try {
      const res = await certificationApi.getCertifications();
      if (res.code === "200") {
        setCertifications(res.certifications || []);
      } else {
        setCertificationError(MESSAGES.ERRORS.SYSTEM_ERROR);
      }
    } catch (err) {
      console.error('Failed to fetch certifications', err);
      setCertificationError(MESSAGES.ERRORS.SYSTEM_ERROR);
    } finally {
      setIsLoadingCertifications(false);
    }
  }, []);

  useEffect(() => {
    fetchCertifications();
  }, [fetchCertifications]);

  return {
    certifications,
    isLoadingCertifications,
    certificationError,
    fetchCertifications,
  };
}
