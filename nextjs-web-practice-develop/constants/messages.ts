import { ERROR_CODES } from './codes';

/**
 * Định nghĩa tập trung tất cả các thông báo của ứng dụng Frontend
 * theo đúng đặc tả tài liệu ADM002 (Mục 8: Thông báo MSG001-MSG005 và Mã lỗi ER001-ER023).
 */
export const MESSAGES = {
  // === Thông báo form xác thực / Login validation ===
  AUTH: {
    USERNAME_REQUIRED: 'Username is required',
    PASSWORD_REQUIRED: 'Password is required',
  },

  // === Thông báo thành công (MSG001 - MSG004) ===
  SUCCESS: {
    /** MSG001: Đăng ký User thành công */
    USER_REGISTERED: 'ユーザの登録が完了しました。',
    /** MSG002: Cập nhật User thành công */
    USER_UPDATED: 'ユーザの更新が完了しました。',
    /** MSG003: Xóa User thành công */
    USER_DELETED: 'ユーザの削除が完了しました。',
    /** MSG004: Xác nhận trước khi xóa */
    CONFIRM_DELETE: '削除しますが、よろしいでしょうか。',
  },

  // === Thông báo trạng thái và tìm kiếm ===
  INFO: {
    /** MSG005: Không tìm thấy user khi tìm kiếm (thay cho 'Không có dữ liệu') */
    NO_DATA: '検索条件に該当するユーザが見つかりません。',
  },

  // === Thông báo lỗi hệ thống và API (ER001 - ER023 & ADM002 mục 8.3) ===
  ERRORS: {
    /** ER023: Lỗi hệ thống chung */
    SYSTEM_ERROR: 'システムエラーが発生しました。',
    /** ADM002 mục 8.3: Khi API lấy danh sách nhân viên thất bại */
    FETCH_EMPLOYEES_FAILED: '従業員を取得できません。',
    /** ADM002 mục 8.3: Khi API lấy danh sách phòng ban thất bại */
    FETCH_DEPARTMENTS_FAILED: '部門を取得できません。',
    /** ER016: Đăng nhập thất bại (sai tài khoản hoặc mật khẩu) */
    LOGIN_FAILED: '「アカウント名」または「パスワード」は不正です。',
    /** ER022: Không tìm thấy trang */
    PAGE_NOT_FOUND: 'ページが見つかりません。',

    /** ER001: Không nhập */
    ER001: (field: string) => `「${field}」を入力してください`,
    /** ER002: Không chọn */
    ER002: (field: string) => `「${field}」を入力してください`,
    /** ER003: Đã tồn tại */
    ER003: (field: string = 'アカウント名') => `「${field}」は既に存在しています。`,
    /** ER004: Không tồn tại */
    ER004: (field: string = '項目') => `「${field}」は存在していません。`,
    /** ER005: Sai format email */
    ER005: (field: string, format = 'email') => `「${field}」を${format}形式で入力してください`,
    /** ER006: Check maxlength */
    ER006: (field: string, max: number) => `${max}桁以内の「${field}」を入力してください`,
    /** ER007: Độ dài trong khoảng min - max */
    ER007: (field: string, min: number, max: number) => `「${field}」を${min}＜＝桁数、＜＝${max}桁で入力してください`,
    /** ER008: Ký tự 1 byte / halfsize */
    ER008: (field: string) => `「${field}」に半角英数を入力してください`,
    /** ER009: Ký tự Kana */
    ER009: (field: string) => `「${field}」をカタカナで入力してください`,
    /** ER011: Ngày không hợp lệ */
    ER011: (field: string) => `「${field}」は無効になっています。`,
    /** ER012: Ngày hết hạn phải lớn hơn ngày cấp */
    ER012: () => `「失効日」は「資格交付日」より未来の日で入力してください。`,
    /** ER013 / ER014: User không tồn tại */
    ER013: () => `該当するユーザは存在していません。`,
    ER014: () => `該当するユーザは存在していません。`,
    /** ER015: Lỗi hệ thống */
    ER015: () => `システムエラーが発生しました。`,
    /** ER017: Mật khẩu xác nhận không trùng khớp */
    ER017: () => `「パスワード（確認）」が不正です。`,
    /** ER018: Số halfsize */
    ER018: (field: string) => `「${field}」は半角で入力してください。`,
    /** ER019: Định dạng tên đăng nhập */
    ER019: () => `[アカウント名]は(a-z, A-Z, 0-9 と _)の桁のみです。最初の桁は数字ではない。`,
    /** ER020: Không thể xóa tài khoản Admin */
    ER020: () => `管理者ユーザを削除することはできません。`,
    /** ER021: Lỗi sắp xếp */
    ER021: () => `ソートは (ASC, DESC) でなければなりません。`,
    /** ER023: Lỗi hệ thống chung */
    ER023: () => `システムエラーが発生しました。`,
  },

  // === Mapping chuẩn theo mã Spec (Mục 8) ===
  MSG: {
    MSG001: 'ユーザの登録が完了しました。',
    MSG002: 'ユーザの更新が完了しました。',
    MSG003: 'ユーザの削除が完了しました。',
    MSG004: '削除しますが、よろしいでしょうか。',
    MSG005: '検索条件に該当するユーザが見つかりません。',
  },

  // === Message riêng cho từng màn hình theo ADM002 mục 8.3 ===
  ADM002: {
    FETCH_DEPARTMENTS_ERROR: '部門を取得できません',
    FETCH_EMPLOYEES_ERROR: '従業員を取得できません',
  },
} as const;

/**
 * Định dạng thông báo lỗi dựa trên mã code và danh sách tham số nhận từ Backend API.
 *
 * @param code Mã lỗi nhận từ server (ER001 - ER023...)
 * @param params Danh sách tham số đính kèm
 * @returns Thông báo lỗi hiển thị bằng tiếng Nhật theo quy chuẩn
 */
export function formatErrorMessage(code?: string, params: (string | number)[] = []): string {
  if (!code) return MESSAGES.ERRORS.SYSTEM_ERROR;

  const p0 = params[0] !== undefined ? String(params[0]) : '';
  const p1 = params[1] !== undefined ? params[1] : '';
  const p2 = params[2] !== undefined ? params[2] : '';

  switch (code) {
    case ERROR_CODES.ER001:
      return MESSAGES.ERRORS.ER001(p0 || 'アカウント名');
    case ERROR_CODES.ER002:
      return MESSAGES.ERRORS.ER002(p0 || '項目');
    case ERROR_CODES.ER003:
      return MESSAGES.ERRORS.ER003(p0 || 'アカウント名');
    case ERROR_CODES.ER004:
      return MESSAGES.ERRORS.ER004(p0 || '項目');
    case ERROR_CODES.ER005:
      return MESSAGES.ERRORS.ER005(p0 || '項目', String(p1 || 'メール'));
    case ERROR_CODES.ER006:
      return MESSAGES.ERRORS.ER006(p0 || '項目', Number(p1) || 50);
    case ERROR_CODES.ER007:
      return MESSAGES.ERRORS.ER007(p0 || '項目', Number(p1) || 8, Number(p2) || 50);
    case ERROR_CODES.ER008:
      return MESSAGES.ERRORS.ER008(p0 || '項目');
    case ERROR_CODES.ER009:
      return MESSAGES.ERRORS.ER009(p0 || '項目');
    case ERROR_CODES.ER011:
      return MESSAGES.ERRORS.ER011(p0 || '項目');
    case ERROR_CODES.ER012:
      return MESSAGES.ERRORS.ER012();
    case ERROR_CODES.ER013:
    case ERROR_CODES.ER014:
      return MESSAGES.ERRORS.ER013();
    case ERROR_CODES.ER015:
      return MESSAGES.ERRORS.ER015();
    case ERROR_CODES.ER016:
      return MESSAGES.ERRORS.LOGIN_FAILED;
    case ERROR_CODES.ER017:
      return MESSAGES.ERRORS.ER017();
    case ERROR_CODES.ER018:
      return MESSAGES.ERRORS.ER018(p0 || '項目');
    case ERROR_CODES.ER019:
      return MESSAGES.ERRORS.ER019();
    case ERROR_CODES.ER020:
      return MESSAGES.ERRORS.ER020();
    case ERROR_CODES.ER021:
      return MESSAGES.ERRORS.ER021();
    case ERROR_CODES.ER023:
      return MESSAGES.ERRORS.ER023();
    default:
      return MESSAGES.ERRORS.SYSTEM_ERROR;
  }
}
