// types/department.ts

/**
 * Thông tin chi tiết phòng ban (Department DTO).
 */
export interface DepartmentDTO {
  departmentId: string;
  departmentName: string;
}

/**
 * Cấu trúc Response trả về từ API lấy danh sách phòng ban (/departments).
 */
export interface ListDepartmentResponse {
  code: string;
  departments: DepartmentDTO[];
}
