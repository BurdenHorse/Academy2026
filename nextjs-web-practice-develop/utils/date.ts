import { REGEX_DATE_FORMAT } from './regex';

export { REGEX_DATE_FORMAT };

/**
 * Kiểm tra tính hợp lệ của chuỗi ngày tháng theo lịch thực tế (yyyy/MM/dd).
 * Đảm bảo ngày trong tháng, tháng 1-12 và năm nhuận đều chính xác.
 *
 * @param dateStr Chuỗi ngày cần kiểm tra
 * @returns boolean true nếu là ngày hợp lệ theo lịch
 */
export function isValidCalendarDate(dateStr: string): boolean {
  if (!dateStr || !REGEX_DATE_FORMAT.test(dateStr)) return false;
  const [yearStr, monthStr, dayStr] = dateStr.split('/');
  const year = parseInt(yearStr, 10);
  const month = parseInt(monthStr, 10);
  const day = parseInt(dayStr, 10);

  if (month < 1 || month > 12) return false;
  if (day < 1 || day > 31) return false;

  const date = new Date(year, month - 1, day);
  return (
    date.getFullYear() === year &&
    date.getMonth() === month - 1 &&
    date.getDate() === day
  );
}

/**
 * Chuyển đổi đối tượng Date sang chuỗi theo định dạng yyyy/MM/dd.
 *
 * @param date Đối tượng Date
 * @returns string Chuỗi ngày định dạng yyyy/MM/dd hoặc chuỗi rỗng
 */
export function formatDateToString(date: Date | null | undefined): string {
  if (!date) return '';
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}/${month}/${day}`;
}

/**
 * Chuyển đổi chuỗi yyyy/MM/dd sang đối tượng Date.
 *
 * @param dateStr Chuỗi ngày định dạng yyyy/MM/dd
 * @returns Date | null Đối tượng Date nếu chuỗi hợp lệ, ngược lại null
 */
export function parseStringToDate(dateStr: string): Date | null {
  if (!dateStr || !isValidCalendarDate(dateStr)) return null;
  const [year, month, day] = dateStr.split('/').map(Number);
  return new Date(year, month - 1, day);
}
