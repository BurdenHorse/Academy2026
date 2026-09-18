/**
 * Định nghĩa tập trung tất cả các URL API Endpoints gọi sang backend.
 */
export const API_ENDPOINTS = {
  /** API đăng nhập */
  LOGIN: '/login',

  /** API danh mục phòng ban */
  DEPARTMENTS: '/departments',

  /** API danh mục chứng chỉ */
  CERTIFICATIONS: '/certifications',

  /** API quản lý nhân viên (CRUD) */
  EMPLOYEE: '/employee',
} as const;
