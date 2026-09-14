// types/certification.ts

/**
 * DTO chứa thông tin chứng chỉ tiếng Nhật.
 */
export interface CertificationDTO {
  certificationId: string;
  certificationName: string;
  certificationLevel?: number;
}

/**
 * Cấu trúc Response trả về từ API lấy danh sách chứng chỉ (/certifications).
 */
export interface ListCertificationResponse {
  code: string;
  certifications: CertificationDTO[];
}

/**
 * Cấu trúc thông tin chứng chỉ kèm theo khi tạo mới hoặc cập nhật nhân viên.
 */
export interface EmployeeCertificationFormDTO {
  certificationId: number;
  certificationStartDate: string; // yyyy/MM/dd
  certificationEndDate: string;   // yyyy/MM/dd
  employeeCertificationScore: number;
}
