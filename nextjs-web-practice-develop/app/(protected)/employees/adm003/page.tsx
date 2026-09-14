'use client';

import { Suspense } from 'react';
import { useAuth } from '@/hooks/useAuth';
import ADM003 from '@/components/employees/adm003';

/**
 * Trang xem chi tiết nhân viên (ADM003 Page).
 * Đóng vai trò Page Container: Kiểm tra Auth và render Component ADM003.
 */
export default function EmployeeDetailPage() {
  useAuth();

  return (
    <Suspense fallback={<div>読み込み中...</div>}>
      <ADM003 />
    </Suspense>
  );
}
