'use client';

import { Suspense } from 'react';
import { useAuth } from '@/hooks/useAuth';
import ADM005 from '@/components/employees/adm005';

/**
 * Trang xác nhận thông tin nhân viên (ADM005 Page).
 * Đóng vai trò Page Container: Kiểm tra Auth và render Component ADM005.
 */
export default function EmployeeConfirmPage() {
  useAuth();

  return (
    <Suspense fallback={<div>読み込み中...</div>}>
      <ADM005 />
    </Suspense>
  );
}
