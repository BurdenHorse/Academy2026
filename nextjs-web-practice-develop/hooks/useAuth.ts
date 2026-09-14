import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { getToken, isTokenExpired } from '@/lib/auth/token';
import { ROUTES } from '@/constants';

/**
 * Custom hook bảo vệ các route yêu cầu xác thực (Protected Routes).
 * Tự động kiểm tra token trong sessionStorage, nếu chưa đăng nhập hoặc token hết hạn
 * sẽ chuyển hướng người dùng về trang đăng nhập.
 */
const useAuth = (): void => {
  const router = useRouter();

  useEffect(() => {
    const token = getToken();
    if (!token || isTokenExpired(token?.accessToken)) {
      router.push(ROUTES.LOGIN);
    }
  }, [router]);
};

/**
 * Custom hook dành cho các route công khai của khách (Guest Routes - ví dụ trang Login).
 * Nếu người dùng đã đăng nhập và token còn hạn, tự động chuyển hướng vào trang danh sách nhân viên (ADM002).
 */
const useGuest = (): void => {
  const router = useRouter();

  useEffect(() => {
    const token = getToken();
    if (token && !isTokenExpired(token?.accessToken)) {
      router.push(ROUTES.EMPLOYEES.LIST);
    }
  }, [router]);
};

export { useAuth, useGuest };
