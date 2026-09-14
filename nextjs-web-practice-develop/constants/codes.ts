/**
 * Định nghĩa tập trung tất cả các mã trạng thái response, message code và error code
 * theo đúng đặc tả tài liệu ADM002 và TKAPI_Tong_hop.md.
 */

/** Mã trạng thái HTTP dạng chuỗi trả về từ Backend API */
export const RESPONSE_CODES = {
  SUCCESS: '200',
  SERVER_ERROR: '500',
} as const;

/** Mã thông báo nghiệp vụ (MSG001 - MSG005) */
export const MESSAGE_CODES = {
  /** MSG001: Đăng ký User thành công */
  MSG001: 'MSG001',
  /** MSG002: Cập nhật User thành công */
  MSG002: 'MSG002',
  /** MSG003: Xóa User thành công */
  MSG003: 'MSG003',
  /** MSG004: Xác nhận trước khi xóa */
  MSG004: 'MSG004',
  /** MSG005: Không tìm thấy user khi tìm kiếm */
  MSG005: 'MSG005',
} as const;

/** Danh sách mã lỗi chuẩn từ Backend API (ER001 - ER023) */
export const ERROR_CODES = {
  /** ER001: Bắt buộc nhập */
  ER001: 'ER001',
  /** ER002: Bắt buộc chọn */
  ER002: 'ER002',
  /** ER003: Đã tồn tại trong hệ thống */
  ER003: 'ER003',
  /** ER004: Không tồn tại trong hệ thống */
  ER004: 'ER004',
  /** ER005: Sai định dạng */
  ER005: 'ER005',
  /** ER006: Vượt quá số ký tự tối đa */
  ER006: 'ER006',
  /** ER007: Độ dài ký tự trong khoảng min - max */
  ER007: 'ER007',
  /** ER008: Ký tự halfsize */
  ER008: 'ER008',
  /** ER009: Ký tự Katakana */
  ER009: 'ER009',
  /** ER011: Ngày tháng không hợp lệ trên lịch */
  ER011: 'ER011',
  /** ER012: Ngày hết hạn phải sau ngày cấp */
  ER012: 'ER012',
  /** ER013: User không tồn tại (xem chi tiết / cập nhật) */
  ER013: 'ER013',
  /** ER014: User không tồn tại (khi xóa) */
  ER014: 'ER014',
  /** ER015: Thao tác thất bại / lỗi hệ thống */
  ER015: 'ER015',
  /** ER016: Đăng nhập thất bại */
  ER016: 'ER016',
  /** ER017: Mật khẩu xác nhận không khớp */
  ER017: 'ER017',
  /** ER018: Số halfsize / số nguyên dương */
  ER018: 'ER018',
  /** ER019: Định dạng tên đăng nhập */
  ER019: 'ER019',
  /** ER020: Không thể xóa tài khoản Admin */
  ER020: 'ER020',
  /** ER021: Lỗi tham số sắp xếp */
  ER021: 'ER021',
  /** ER022: Không tìm thấy trang */
  ER022: 'ER022',
  /** ER023: Lỗi hệ thống chung */
  ER023: 'ER023',
} as const;
