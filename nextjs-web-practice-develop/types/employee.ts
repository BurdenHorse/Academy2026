// types/employee.ts

import { EmployeeCertificationFormDTO } from './certification';

/**
 * Thông tin nhân viên trong danh sách trả về từ Backend cho màn hình ADM002.
 */
export interface EmployeeListDTO {
  employeeId: string;
  employeeName: string;
  employeeBirthDate?: string; // yyyy/MM/dd
  departmentName: string;
  employeeEmail: string;
  employeeTelephone?: string;
  certificationName?: string;
  endDate?: string; // yyyy/MM/dd
  score?: string;
}

/**
 * Cấu trúc Response trả về từ API lấy danh sách nhân viên (/employee).
 */
export interface EmployeeListResponse {
  code: string;
  totalRecords: number;
  employees: EmployeeListDTO[];
}

/**
 * Các tham số query gửi lên API tìm kiếm, phân trang và sắp xếp nhân viên.
 */
export interface EmployeeSearchParams {
  employee_name?: string;
  department_id?: string;
  ord_employee_name?: 'ASC' | 'DESC' | '';
  ord_certification_name?: 'ASC' | 'DESC' | '';
  ord_end_date?: 'ASC' | 'DESC' | '';
  sort_priority?: string;
  offset?: number;
  limit?: number;
}

/**
 * Form dữ liệu nhập liệu trên màn hình ADM004.
 */
export interface EmployeeFormData {
  employeeLoginId: string;
  departmentId: string;
  employeeName: string;
  employeeNameKana: string;
  employeeBirthDate: string; // yyyy/MM/dd
  employeeEmail: string;
  employeeTelephone: string;
  employeeLoginPassword: string;
  employeeLoginPasswordConfirm: string;
  certificationId: string;
  certificationStartDate: string; // yyyy/MM/dd
  certificationEndDate: string;   // yyyy/MM/dd
  employeeCertificationScore: string;
}

/**
 * Payload gửi lên backend để tạo mới nhân viên (POST /employee).
 */
export interface AddEmployeePayload {
  employeeName: string;
  employeeBirthDate: string; // yyyy/MM/dd
  employeeEmail: string;
  employeeTelephone: string;
  employeeNameKana: string;
  employeeLoginId: string;
  employeeLoginPassword: string;
  departmentId: number;
  certifications?: EmployeeCertificationFormDTO[];
}

/**
 * Response trả về từ API tạo mới nhân viên (POST /employee).
 */
export interface AddEmployeeResponse {
  code: string;
  employeeId: number;
  message?: {
    code: string;
    params?: string[];
  };
}

/**
 * Chi tiết một chứng chỉ tiếng Nhật trong thông tin chi tiết nhân viên.
 */
export interface EmployeeCertificationDetailDTO {
  certificationId: number;
  certificationName: string;
  startDate: string; // yyyy/MM/dd
  endDate: string;   // yyyy/MM/dd
  score: number;
}

/**
 * Response trả về từ API lấy chi tiết nhân viên (GET /employee/{id}).
 */
export interface EmployeeDetailResponse {
  code: string;
  employeeId: number;
  employeeName: string;
  employeeBirthDate: string; // yyyy/MM/dd
  departmentId: number;
  departmentName: string;
  employeeEmail: string;
  employeeTelephone: string;
  employeeNameKana: string;
  employeeLoginId: string;
  role?: number;
  certifications: EmployeeCertificationDetailDTO[];
}

/**
 * Response trả về từ API xóa nhân viên (DELETE /employee/{id}).
 */
export interface DeleteEmployeeResponse {
  code: string;
  employeeId: number;
  message?: {
    code: string;
    params?: string[];
  };
}

/**
 * Payload gửi lên backend để cập nhật thông tin nhân viên (PUT /employee).
 */
export interface UpdateEmployeePayload {
  employeeId: number;
  employeeName: string;
  employeeBirthDate: string; // yyyy/MM/dd
  employeeEmail: string;
  employeeTelephone: string;
  employeeNameKana: string;
  employeeLoginId: string;
  employeeLoginPassword?: string;
  departmentId: number;
  certifications?: EmployeeCertificationFormDTO[];
}

/**
 * Response trả về từ API cập nhật nhân viên (PUT /employee).
 */
export interface UpdateEmployeeResponse {
  code: string;
  employeeId: number;
  message?: {
    code: string;
    params?: string[];
  };
}


