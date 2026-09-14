'use client';

import { useAuth } from '@/hooks/useAuth';
import ADM002 from '@/components/employees/adm002';

/**
 * Trang danh sách nhân viên (ADM002 Page).
 * Đóng vai trò Page Container: Kiểm tra Auth và render Component ADM002.
 */
export default function EmployeeListPage() {
  useAuth();

  return <ADM002 />;
}
