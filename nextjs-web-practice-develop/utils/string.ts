/**
 * Module cung cấp các hàm tiện ích xử lý và định dạng chuỗi (String Utilities).
 */

/**
 * Truncate chuỗi ký tự tối đa theo độ dài chỉ định (mặc định 22 ký tự),
 * thêm dấu ba chấm "..." nếu chuỗi vượt quá độ dài.
 *
 * @param text Chuỗi đầu vào cần cắt ngắn
 * @param maxLength Độ dài tối đa cho phép (mặc định 22)
 * @returns Chuỗi sau khi cắt ngắn hoặc chuỗi rỗng nếu text null/undefined
 */
export function truncateText(text: string | null | undefined, maxLength: number = 22): string {
  if (!text) return '';
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + '...';
}

/**
 * Format điểm số: loại bỏ phần thập phân không cần thiết
 * (ví dụ: "180.00" -> "180", "150.0" -> "150").
 *
 * @param score Điểm số dạng chuỗi hoặc số
 * @returns Chuỗi điểm số đã format
 */
export function formatScore(score: string | number | null | undefined): string {
  if (score === null || score === undefined || score === '') return '';
  const s = String(score).trim();
  return s.replace(/\.00$/, '').replace(/\.0$/, '');
}

/**
 * Kiểm tra xem chuỗi có null, undefined hoặc chỉ chứa khoảng trắng hay không.
 *
 * @param str Chuỗi cần kiểm tra
 * @returns true nếu rỗng hoặc null/undefined
 */
export function isNullOrEmpty(str: string | null | undefined): boolean {
  return !str || str.trim().length === 0;
}
