'use client';

import { Suspense } from 'react';
import { useAuth } from '@/hooks/useAuth';
import ADM004 from '@/components/employees/adm004';

/**
 * Trang thêm mới / chỉnh sửa thông tin nhân viên (ADM004 Page).
 * Đóng vai trò Page Container: Kiểm tra Auth và render Component ADM004.
 */
export default function EmployeeEditPage() {
  useAuth();

  return (
    <Suspense fallback={<div>読み込み中...</div>}>
      <ADM004 />
    </Suspense>
  );
}
