'use client';

import React from 'react';
import { useADM006 } from '@/hooks/useADM006';
import { MESSAGES, LABELS } from '@/constants';

/**
 * Component hiển thị toàn bộ giao diện màn hình Hoàn thành thao tác (ADM006).
 * Sử dụng custom hook useADM006.
 */
export default function ADM006() {
  const { message, handleOk } = useADM006();

  return (
    <div className="box-shadow">
      <div className="notification-box">
        {/* ===== Phần thông báo hoàn thành ===== */}
        <h1 className="msg-title">{message}</h1>
        {/* ===== Phần nút OK ===== */}
        <div className="notification-box-btn">
          <button type="button" onClick={handleOk} className="btn btn-primary btn-sm">
            {LABELS.BUTTONS.OK}
          </button>
        </div>
      </div>
    </div>
  );
}

export { ADM006 };
