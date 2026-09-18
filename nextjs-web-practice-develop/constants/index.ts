export * from './routes';
export * from './messages';
export * from './labels';
export * from './codes';
export * from './roles';
export * from './endpoints';

/** Số bản ghi mặc định trên 1 trang phân trang */
export const DEFAULT_PAGE_LIMIT = 10;

/** Hằng số cho thứ tự sắp xếp danh sách */
export const SORT_DIRECTIONS = {
  ASC: 'ASC',
  DESC: 'DESC',
} as const;

export type SortDirection = (typeof SORT_DIRECTIONS)[keyof typeof SORT_DIRECTIONS];

/** Các cột hỗ trợ sắp xếp trong danh sách nhân viên (ADM002) */
export const SORT_FIELDS = {
  EMPLOYEE_NAME: 'ord_employee_name',
  CERTIFICATION_NAME: 'ord_certification_name',
  END_DATE: 'ord_end_date',
} as const;

export type SortField = (typeof SORT_FIELDS)[keyof typeof SORT_FIELDS];

/** Biểu tượng hiển thị chiều sắp xếp */
export const SORT_ICONS = {
  ASC: '▲▽',
  DESC: '▼△',
} as const;

/** Thứ tự ưu tiên sắp xếp mặc định */
export const DEFAULT_SORT_PRIORITY = `${SORT_FIELDS.EMPLOYEE_NAME},${SORT_FIELDS.CERTIFICATION_NAME},${SORT_FIELDS.END_DATE}`;

/** Các giới hạn validation và độ dài cắt chuỗi hiển thị */
export const VALIDATION_LIMITS = {
  MAX_LOGIN_ID: 50,
  MAX_FULL_NAME: 125,
  MAX_FULL_NAME_KANA: 125,
  MAX_EMAIL: 125,
  MAX_TELEPHONE: 50,
  MIN_PASSWORD: 8,
  MAX_PASSWORD: 50,
  TRUNCATE_TABLE_TEXT: 22,
} as const;

/** Khóa lưu trữ trong sessionStorage / localStorage */
export const STORAGE_KEYS = {
  TOKEN: 'access_token',
  ACCESS_TOKEN: 'access_token',
  TOKEN_TYPE: 'token_type',
  FORM_DATA: 'ADM004_FORM_DATA',
  SERVER_ERROR: 'ADM004_SERVER_ERROR',
  DETAIL_EMPLOYEE_ID: 'ADM003_EMPLOYEE_ID',
  COMPLETE_MESSAGE_CODE: 'ADM006_MESSAGE_CODE',
  SYSTEM_ERROR_MESSAGE: 'SYSTEM_ERROR_MESSAGE',
  SEARCH_PARAMS: 'ADM002_SEARCH_PARAMS',
  SEARCH_INPUT: 'ADM002_SEARCH_INPUT',
} as const;

