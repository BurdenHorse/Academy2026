'use client';

import React, { useEffect, useState, Suspense } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { ROUTES, MESSAGES, LABELS, STORAGE_KEYS } from '@/constants';

function SystemErrorContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [errorMessage, setErrorMessage] = useState<string>(MESSAGES.ERRORS.ER023());

  useEffect(() => {
    if (typeof window !== 'undefined') {
      const storedMsg = sessionStorage.getItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE);
      const queryMsg = searchParams ? searchParams.get('message') : null;
      
      if (storedMsg) {
        setErrorMessage(storedMsg);
        sessionStorage.removeItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE);
      } else if (queryMsg) {
        setErrorMessage(queryMsg);
      }
    }
  }, [searchParams]);

  /**
   * Xử lý khi nhấn nút OK:
   * - Nếu đã đăng nhập: chuyển về trang danh sách nhân viên ADM002 (trang 1).
   * - Nếu chưa đăng nhập: chuyển về trang đăng nhập ADM001.
   */
  const handleOk = () => {
    const token = typeof window !== 'undefined' ? sessionStorage.getItem('access_token') : null;
    if (token) {
      router.push(ROUTES.EMPLOYEES.LIST);
    } else {
      router.push(ROUTES.LOGIN);
    }
  };

  return (
    <div className="box-shadow">
      <div className="notification-box">
        <h1 className="msg-title text-danger" style={{ color: '#d9534f' }}>
          {errorMessage}
        </h1>
        <div className="notification-box-btn">
          <button type="button" onClick={handleOk} className="btn btn-primary btn-sm">
            {LABELS.BUTTONS.OK}
          </button>
        </div>
      </div>
    </div>
  );
}

/**
 * Trang thông báo lỗi hệ thống (System Error).
 * Theo đặc tả tài liệu TKMH_Tong_hop.md - Mục 260.
 */
export default function SystemErrorPage() {
  return (
    <Suspense fallback={<div>読み込み中...</div>}>
      <SystemErrorContent />
    </Suspense>
  );
}
