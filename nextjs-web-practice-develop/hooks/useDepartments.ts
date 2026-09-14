import { useState, useEffect, useCallback } from 'react';
import { departmentApi } from '@/lib/api/department';
import { DepartmentDTO } from '@/types/department';
import { MESSAGES } from '@/constants';

/**
 * Custom hook quản lý việc lấy danh sách và state của phòng ban (Departments).
 *
 * @returns Object chứa danh sách departments, trạng thái loading, lỗi và hàm fetchDepartments
 */
export function useDepartments() {
  const [departments, setDepartments] = useState<DepartmentDTO[]>([]);
  const [isLoadingDepartments, setIsLoadingDepartments] = useState<boolean>(true);
  const [departmentError, setDepartmentError] = useState<string | null>(null);

  /**
   * Gọi API lấy danh sách phòng ban từ server.
   */
  const fetchDepartments = useCallback(async () => {
    setIsLoadingDepartments(true);
    setDepartmentError(null);
    try {
      const res = await departmentApi.getDepartments();
      if (res.code === "200") {
        setDepartments(res.departments || []);
      } else {
        setDepartmentError(MESSAGES.ADM002.FETCH_DEPARTMENTS_ERROR);
      }
    } catch (err) {
      console.error('Failed to fetch departments', err);
      setDepartmentError(MESSAGES.ERRORS.FETCH_DEPARTMENTS_FAILED);
    } finally {
      setIsLoadingDepartments(false);
    }
  }, []);

  useEffect(() => {
    fetchDepartments();
  }, [fetchDepartments]);

  return {
    departments,
    isLoadingDepartments,
    departmentError,
    fetchDepartments,
  };
}
