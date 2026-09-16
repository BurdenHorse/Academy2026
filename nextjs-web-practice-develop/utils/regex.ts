/**
 * Định nghĩa tập trung các biểu thức chính quy (Regex) dùng chung trong toàn ứng dụng.
 */

/** Regex kiểm tra định dạng chuỗi ngày tháng yyyy/MM/dd */
export const REGEX_DATE_FORMAT = /^\d{4}\/\d{2}\/\d{2}$/;

/** Regex kiểm tra định dạng tên đăng nhập (bắt đầu bằng chữ cái hoặc _, không bắt đầu bằng số) */
export const REGEX_LOGIN_ID = /^[a-zA-Z_][a-zA-Z0-9_]*$/;

/** Regex kiểm tra ký tự Katakana nửa byte (half-width Katakana: ｦ - ﾟ và khoảng trắng) */
export const REGEX_KANA = /^[\uFF66-\uFF9F\s]+$/;

/** Regex kiểm tra định dạng email tiêu chuẩn */
export const REGEX_EMAIL = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

/** Regex kiểm tra ký tự ASCII nửa byte (half-width ASCII) */
export const REGEX_HALFSIZE_ASCII = /^[\x20-\x7E]+$/;

/** Regex kiểm tra toàn bộ ký tự nửa byte bao gồm cả Katakana nửa byte */
export const REGEX_ALL_HALFSIZE = /^[\x20-\x7E\uFF61-\uFF9F]+$/;

/** Regex kiểm tra số điện thoại (chấp nhận half-width số, chữ, dấu +, -, (), và khoảng trắng) */
export const REGEX_HALFSIZE_TEL = /^[a-zA-Z0-9+() -]+$/;

/** Regex kiểm tra chỉ chứa số nửa byte */
export const REGEX_HALFSIZE_NUM = /^[0-9]+$/;
