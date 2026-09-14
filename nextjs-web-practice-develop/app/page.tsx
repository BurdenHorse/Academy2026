import { redirect } from 'next/navigation';
import { ROUTES } from '@/constants';

/**
 * Trang gốc ("/") tự động chuyển hướng (redirect) người dùng về trang đăng nhập (/login - ADM001).
 *
 * @returns null
 */
export default function RootPage(): null {
  redirect(ROUTES.LOGIN);
  return null;
}
