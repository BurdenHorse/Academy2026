'use client';

import { useAuth } from '@/hooks/useAuth';
import ADM006 from '@/components/employees/adm006';

/**
 * Trang thông báo hoàn thành (ADM006 Page).
 * Đóng vai trò Page Container: Kiểm tra Auth và render Component ADM006.
 */
export default function EmployeeCompletePage() {
  useAuth();

  return <ADM006 />;
}
