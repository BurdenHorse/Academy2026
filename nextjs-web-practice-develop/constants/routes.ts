/**
 * Định nghĩa tập trung tất cả các đường dẫn (routes) của ứng dụng Frontend.
 */
export const ROUTES = {
  /** Trang đăng nhập (ADM001) */
  LOGIN: '/login',

  /** Trang đăng xuất */
  LOGOUT: '/logout',

  /** Các màn hình quản lý nhân viên */
  EMPLOYEES: {
    /** Danh sách nhân viên (ADM002) */
    LIST: '/employees/adm002',

    /** Xem chi tiết nhân viên (ADM003) */
    DETAIL: '/employees/adm003',

    /** Thêm mới / Chỉnh sửa nhân viên (ADM004) */
    ADD_EDIT: '/employees/adm004',

    /** Xác nhận thông tin nhân viên (ADM005) */
    CONFIRM: '/employees/adm005',

    /** Thông báo hoàn thành thao tác (ADM006) */
    COMPLETE: '/employees/adm006',
  },

  /** Trang lỗi hệ thống (System Error) */
  SYSTEM_ERROR: '/system-error',
} as const;
