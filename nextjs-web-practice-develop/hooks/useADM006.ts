import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ROUTES, MESSAGES, STORAGE_KEYS, MESSAGE_CODES } from '@/constants';

/**
 * Custom hook quản lý thông báo và hành động cho màn hình Hoàn thành (ADM006).
 *
 * @returns Object chứa message text và handlers cho ADM006
 */
export function useADM006() {
  const router = useRouter();
  const [message, setMessage] = useState<string>(MESSAGES.MSG.MSG001);

  useEffect(() => {
    if (typeof window !== 'undefined') {
      const messageCode = sessionStorage.getItem(STORAGE_KEYS.COMPLETE_MESSAGE_CODE);
      if (messageCode === MESSAGE_CODES.MSG003) {
        setMessage(MESSAGES.MSG.MSG003);
      } else if (messageCode === MESSAGE_CODES.MSG002) {
        setMessage(MESSAGES.MSG.MSG002);
      } else {
        setMessage(MESSAGES.MSG.MSG001);
      }
      sessionStorage.removeItem(STORAGE_KEYS.COMPLETE_MESSAGE_CODE);
    }
  }, []);

  /**
   * Chuyển hướng quay về màn hình danh sách nhân viên (ADM002).
   */
  const handleOk = () => {
    router.push(ROUTES.EMPLOYEES.LIST);
  };

  return {
    message,
    handleOk,
  };
}
