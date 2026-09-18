import { useState, useEffect, useCallback } from 'react';
import { employeeApi } from '@/lib/api/employee';
import { EmployeeListDTO, EmployeeSearchParams } from '@/types/employee';
import {
  DEFAULT_PAGE_LIMIT,
  MESSAGES,
  RESPONSE_CODES,
  STORAGE_KEYS,
  SORT_DIRECTIONS,
  SORT_FIELDS,
  SORT_ICONS,
  DEFAULT_SORT_PRIORITY,
  SortField,
} from '@/constants';
import { truncateText, formatScore } from '@/utils';
import { useDepartments } from './useDepartments';

export { truncateText, formatScore };

/**
 * Custom hook quản lý toàn bộ state và logic nghiệp vụ của màn hình danh sách nhân viên (ADM002).
 * Hỗ trợ:
 * - Tái sử dụng hook useDepartments để lấy danh mục phòng ban.
 * - Lưu và khôi phục trạng thái tìm kiếm/phân trang/sắp xếp qua sessionStorage.
 * - Toggle sort 1-click thay đổi ngay chiều sắp xếp và icon.
 * - Giới hạn độ dài text hiển thị.
 *
 * @returns Object chứa các state và action handlers của ADM002
 */
export function useADM002() {
  // Tái sử dụng custom hook useDepartments
  const { departments, departmentError } = useDepartments();

  // State cho danh sách nhân viên và tổng số bản ghi
  const [employees, setEmployees] = useState<EmployeeListDTO[]>([]);
  const [totalRecords, setTotalRecords] = useState<number>(0);

  // State quản lý trạng thái tải và thông báo lỗi UI
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  // Khôi phục searchParams từ sessionStorage nếu có, luôn đồng bộ limit theo DEFAULT_PAGE_LIMIT mới nhất
  const [searchParams, setSearchParams] = useState<EmployeeSearchParams>(() => {
    if (typeof window !== 'undefined') {
      const saved = sessionStorage.getItem(STORAGE_KEYS.SEARCH_PARAMS);
      if (saved) {
        try {
          const parsed = JSON.parse(saved);
          return {
            ...parsed,
            limit: DEFAULT_PAGE_LIMIT,
          };
        } catch {
          // ignore
        }
      }
    }
    return {
      employee_name: '',
      department_id: '',
      ord_employee_name: SORT_DIRECTIONS.ASC,
      ord_certification_name: '',
      ord_end_date: '',
      sort_priority: DEFAULT_SORT_PRIORITY,
      offset: 0,
      limit: DEFAULT_PAGE_LIMIT,
    };
  });

  // State lưu trữ dữ liệu người dùng nhập trên form tìm kiếm trước khi bấm Search
  const [searchInput, setSearchInput] = useState<{ employee_name: string; department_id: string }>(() => {
    if (typeof window !== 'undefined') {
      const saved = sessionStorage.getItem(STORAGE_KEYS.SEARCH_INPUT);
      if (saved) {
        try {
          return JSON.parse(saved);
        } catch {
          // ignore
        }
      }
    }
    return {
      employee_name: '',
      department_id: '',
    };
  });

  // Lưu searchParams & searchInput vào sessionStorage mỗi khi thay đổi
  useEffect(() => {
    if (typeof window !== 'undefined') {
      sessionStorage.setItem(STORAGE_KEYS.SEARCH_PARAMS, JSON.stringify(searchParams));
      sessionStorage.setItem(STORAGE_KEYS.SEARCH_INPUT, JSON.stringify(searchInput));
    }
  }, [searchParams, searchInput]);

  /**
   * Gọi API lấy danh sách nhân viên dựa trên các điều kiện searchParams hiện tại.
   */
  const fetchEmployees = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const employeeListResponse = await employeeApi.getEmployees(searchParams);
      if (employeeListResponse.code === RESPONSE_CODES.SUCCESS) {
        setEmployees(employeeListResponse.employees || []);
        setTotalRecords(employeeListResponse.totalRecords || 0);
      } else {
        setError(MESSAGES.ADM002.FETCH_EMPLOYEES_ERROR);
      }
    } catch (fetchError) {
      console.error('Failed to fetch employees', fetchError);
      setError(MESSAGES.ERRORS.SYSTEM_ERROR);
    } finally {
      setIsLoading(false);
    }
  }, [searchParams]);

  useEffect(() => {
    fetchEmployees();
  }, [fetchEmployees]);

  /**
   * Xử lý khi người dùng nhấn nút 'Tìm kiếm' (検索).
   * Cập nhật các điều kiện tìm kiếm từ searchInput vào searchParams và reset về trang đầu tiên (offset = 0).
   */
  const handleSearch = () => {
    setSearchParams(prev => ({
      ...prev,
      employee_name: searchInput.employee_name,
      department_id: searchInput.department_id,
      offset: 0, // Reset về trang đầu tiên
      limit: DEFAULT_PAGE_LIMIT,
    }));
  };

  // Tính toán tổng số trang và trang hiện tại
  const totalPages = Math.ceil(totalRecords / DEFAULT_PAGE_LIMIT);
  const currentPage = Math.floor((searchParams.offset || 0) / DEFAULT_PAGE_LIMIT) + 1;

  /**
   * Xử lý khi người dùng chuyển sang một trang mới trong thanh phân trang.
   *
   * @param newPage Số thứ tự trang mới cần chuyển đến (1-based)
   */
  const handlePageChange = (newPage: number) => {
    if (newPage < 1 || newPage > totalPages) return;
    setSearchParams(prev => ({
      ...prev,
      offset: (newPage - 1) * DEFAULT_PAGE_LIMIT,
    }));
  };

  /**
   * Trả về biểu tượng icon sort cho một cột cụ thể (▲▽ khi ASC, ▼△ khi DESC).
   *
   * @param field Tên trường sort
   * @returns Chuỗi ký tự biểu diễn icon mũi tên ('▲▽' hoặc '▼△')
   */
  const getSortIcon = (field: SortField) => {
    const currentSort = searchParams[field];
    if (currentSort === SORT_DIRECTIONS.DESC) return SORT_ICONS.DESC;
    return SORT_ICONS.ASC;
  };

  /**
   * Xử lý sự kiện khi người dùng click vào tiêu đề cột để sắp xếp.
   * Fix bug: Toggle ngay lập tức ở lần click đầu tiên (ASC -> DESC -> ASC).
   *
   * @param field Tên trường sort được click
   */
  const handleSort = (field: SortField) => {
    setSearchParams(prev => {
      const newParams = { ...prev, offset: 0 };
      const currentVal = prev[field] || SORT_DIRECTIONS.ASC;

      // Toggle ngay lập tức: nếu đang ASC -> sang DESC, nếu đang DESC -> sang ASC
      newParams[field] = currentVal === SORT_DIRECTIONS.ASC ? SORT_DIRECTIONS.DESC : SORT_DIRECTIONS.ASC;

      // Đảm bảo các cột khác có giá trị mặc định ASC nếu chưa set
      const allSortFields: SortField[] = [
        SORT_FIELDS.EMPLOYEE_NAME,
        SORT_FIELDS.CERTIFICATION_NAME,
        SORT_FIELDS.END_DATE,
      ];
      allSortFields.forEach(f => {
        if (f !== field && !newParams[f]) {
          newParams[f] = SORT_DIRECTIONS.ASC;
        }
      });

      // Đẩy cột được click lên đầu priority
      const currentPriority = (prev.sort_priority || '').split(',').filter(Boolean);
      const newPriority = [field, ...currentPriority.filter(f => f !== field)];
      newParams.sort_priority = newPriority.join(',');

      return newParams;
    });
  };

  return {
    departments,
    employees,
    totalRecords,
    isLoading,
    error: error || departmentError,
    searchParams,
    searchInput,
    setSearchInput,
    handleSearch,
    handlePageChange,
    handleSort,
    getSortIcon,
    totalPages,
    currentPage,
    truncateText,
    formatScore,
  };
}
