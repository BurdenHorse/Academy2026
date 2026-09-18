import { STORAGE_KEYS } from '@/constants';

/**
 * Lưu trữ Access Token và Token Type vào sessionStorage của trình duyệt.
 *
 * @param token Chuỗi JWT access token nhận từ server
 * @param tokenType Loại token (thường là 'Bearer')
 */
export function storeToken(token: string, tokenType: string): void {
  sessionStorage.setItem(STORAGE_KEYS.ACCESS_TOKEN, token);
  sessionStorage.setItem(STORAGE_KEYS.TOKEN_TYPE, tokenType);
}

/**
 * Lấy Access Token và Token Type từ sessionStorage.
 *
 * @returns Object chứa accessToken và tokenType, hoặc null nếu chưa đăng nhập
 */
export function getToken(): { accessToken: string; tokenType: string } | null {
  const accessToken = sessionStorage.getItem(STORAGE_KEYS.ACCESS_TOKEN);
  const tokenType = sessionStorage.getItem(STORAGE_KEYS.TOKEN_TYPE);

  if (accessToken && tokenType) {
    return { accessToken, tokenType };
  }
  return null;
}

/**
 * Xóa thông tin Token khỏi sessionStorage khi người dùng đăng xuất hoặc hết hạn phiên làm việc.
 */
export function removeToken(): void {
  sessionStorage.removeItem(STORAGE_KEYS.ACCESS_TOKEN);
  sessionStorage.removeItem(STORAGE_KEYS.TOKEN_TYPE);
}

/**
 * Kiểm tra xem JWT Access Token đã hết hạn hay chưa dựa vào claim 'exp' trong payload.
 *
 * @param token Chuỗi JWT access token cần kiểm tra
 * @returns true nếu token đã hết hạn hoặc không hợp lệ, ngược lại false
 */
export function isTokenExpired(token: string): boolean {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return Date.now() >= payload.exp * 1000;
  } catch {
    return true;
  }
}
