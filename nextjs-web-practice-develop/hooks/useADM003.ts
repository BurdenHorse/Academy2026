import { useState, useEffect, useCallback } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { ROUTES, MESSAGES, LABELS, STORAGE_KEYS, formatErrorMessage, MESSAGE_CODES, ROLES, ERROR_CODES } from '@/constants';
import { employeeApi } from '@/lib/api/employee';
import { EmployeeDetailResponse } from '@/types/employee';
import { REGEX_HALFSIZE_NUM } from '@/utils';

/**
 * Custom hook quản lý dữ liệu và hành động cho màn hình xem chi tiết nhân viên (ADM003).
 * Nhận ID trực tiếp thông qua URL query parameter (?id=...).
 */
export function useADM003() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const id = searchParams.get('id');

  const [employee, setEmployee] = useState<EmployeeDetailResponse | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [isDeleting, setIsDeleting] = useState<boolean>(false);
  const [deleteError, setDeleteError] = useState<string | null>(null);

  // Validate ID và fetch dữ liệu chi tiết nhân viên trực tiếp từ ID trên URL
  useEffect(() => {
    // 1. Kiểm tra ID: nếu thiếu hoặc không phải là số nguyên dương -> chuyển sang /system-error
    if (!id || !REGEX_HALFSIZE_NUM.test(id.trim()) || parseInt(id.trim(), 10) <= 0) {
      sessionStorage.setItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE, MESSAGES.ERRORS.ER018(LABELS.FIELDS.ID_FULLWIDTH));
      router.replace(ROUTES.SYSTEM_ERROR);
      return;
    }

    const trimmedId = id.trim();
    let isMounted = true;
    setIsLoading(true);

    // 2. Gọi API lấy chi tiết nhân viên
    employeeApi
      .getEmployeeDetail(trimmedId)
      .then((employeeDetailData) => {
        if (!isMounted) return;
        // Nếu role là admin (1) -> không cho phép hiển thị, trả về ADM002
        if (employeeDetailData.role === ROLES.ADMIN) {
          router.replace(ROUTES.EMPLOYEES.LIST);
          return;
        }
        setEmployee(employeeDetailData);
        setIsLoading(false);
      })
      .catch((apiError) => {
        if (!isMounted) return;
        setIsLoading(false);
        const code = apiError?.response?.data?.message?.code;
        const params = apiError?.response?.data?.message?.params;
        const errMsg = formatErrorMessage(code, params);
        sessionStorage.setItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE, errMsg);
        router.replace(ROUTES.SYSTEM_ERROR);
      });

    return () => {
      isMounted = false;
    };
  }, [id, router]);

  /**
   * Điều hướng sang màn hình chỉnh sửa nhân viên (ADM004) kèm id trên URL.
   */
  const handleEdit = useCallback(() => {
    if (!id) return;
    router.push(`${ROUTES.EMPLOYEES.ADD_EDIT}?id=${id.trim()}`);
  }, [id, router]);

  /**
   * Xử lý xóa nhân viên.
   */
  const handleDelete = useCallback(async () => {
    if (!id || isDeleting) return;

    // Hiển thị hộp thoại xác nhận MSG004
    const confirmed = window.confirm(MESSAGES.MSG.MSG004);
    if (!confirmed) return;

    setIsDeleting(true);
    setDeleteError(null);

    try {
      await employeeApi.deleteEmployee(id.trim());
      // Lưu MSG003 và chuyển hướng sang ADM006
      sessionStorage.setItem(STORAGE_KEYS.COMPLETE_MESSAGE_CODE, MESSAGE_CODES.MSG003);
      router.push(ROUTES.EMPLOYEES.COMPLETE);
    } catch (deleteApiError: any) {
      setIsDeleting(false);
      const code = deleteApiError?.response?.data?.message?.code;
      const params = deleteApiError?.response?.data?.message?.params;
      const errMsg = formatErrorMessage(code, params) || MESSAGES.ERRORS.ER015();
      setDeleteError(errMsg);
    }
  }, [id, isDeleting, router]);

  /**
   * Quay lại màn hình danh sách nhân viên (ADM002).
   */
  const handleBack = useCallback(() => {
    router.push(ROUTES.EMPLOYEES.LIST);
  }, [router]);

  return {
    employee,
    isLoading,
    isDeleting,
    deleteError,
    handleEdit,
    handleDelete,
    handleBack,
  };
}
