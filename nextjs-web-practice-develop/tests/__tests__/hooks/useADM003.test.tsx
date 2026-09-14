import { renderHook, act, waitFor } from '@testing-library/react';
import { useADM003 } from '@/hooks/useADM003';
import { employeeApi } from '@/lib/api/employee';
import { useRouter, useSearchParams } from 'next/navigation';
import { STORAGE_KEYS, ROUTES } from '@/constants';

jest.mock('next/navigation', () => ({
  useRouter: jest.fn(),
  useSearchParams: jest.fn(),
}));

jest.mock('@/lib/api/employee', () => ({
  employeeApi: {
    getEmployeeDetail: jest.fn(),
    deleteEmployee: jest.fn(),
  },
}));

const mockPush = jest.fn();
const mockReplace = jest.fn();
(useRouter as jest.Mock).mockReturnValue({
  push: mockPush,
  replace: mockReplace,
});

const mockedGetEmployeeDetail = employeeApi.getEmployeeDetail as jest.Mock;
const mockedDeleteEmployee = employeeApi.deleteEmployee as jest.Mock;

describe('useADM003 Hook', () => {
  const sampleEmployeeDetail = {
    code: '200',
    employeeId: 10,
    employeeName: 'Nguyễn Văn A',
    employeeBirthDate: '1990/01/01',
    departmentId: 1,
    departmentName: 'Phòng Phát Triển',
    employeeEmail: 'a@luvina.net',
    employeeTelephone: '0987654321',
    employeeNameKana: 'グエン ヴァン エー',
    employeeLoginId: 'nguyenvana',
    certifications: [
      {
        certificationId: 1,
        certificationName: 'N1',
        startDate: '2020/01/01',
        endDate: '2022/01/01',
        score: 160,
      },
    ],
  };

  beforeEach(() => {
    jest.clearAllMocks();
    sessionStorage.clear();
    (useSearchParams as jest.Mock).mockReturnValue({
      get: jest.fn((key: string) => (key === 'id' ? '10' : null)),
    });
  });

  it('tải thông tin chi tiết nhân viên thành công', async () => {
    mockedGetEmployeeDetail.mockResolvedValue(sampleEmployeeDetail);

    const { result } = renderHook(() => useADM003());

    expect(result.current.isLoading).toBe(true);

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
    });

    expect(result.current.employee).toEqual(sampleEmployeeDetail);
    expect(mockedGetEmployeeDetail).toHaveBeenCalledWith('10');
  });

  it('chuyển hướng sang System Error khi ID không hợp lệ', async () => {
    (useSearchParams as jest.Mock).mockReturnValue({
      get: jest.fn(() => 'invalid_id'),
    });

    renderHook(() => useADM003());

    await waitFor(() => {
      expect(mockReplace).toHaveBeenCalledWith(ROUTES.SYSTEM_ERROR);
    });
  });

  it('chuyển hướng sang System Error khi API trả về lỗi (ví dụ ER013)', async () => {
    mockedGetEmployeeDetail.mockRejectedValue({
      response: {
        data: {
          code: '500',
          message: {
            code: 'ER013',
            params: [],
          },
        },
      },
    });

    renderHook(() => useADM003());

    await waitFor(() => {
      expect(mockReplace).toHaveBeenCalledWith(ROUTES.SYSTEM_ERROR);
    });

    expect(sessionStorage.getItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE)).toBe(
      '該当するユーザは存在していません。'
    );
  });

  it('handleEdit lưu ID và điều hướng sang ADM004', async () => {
    mockedGetEmployeeDetail.mockResolvedValue(sampleEmployeeDetail);

    const { result } = renderHook(() => useADM003());

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
    });

    act(() => {
      result.current.handleEdit();
    });

    expect(mockPush).toHaveBeenCalledWith(`${ROUTES.EMPLOYEES.ADD_EDIT}?id=10`);
  });

  it('handleDelete không gọi API khi người dùng bấm Hủy trên Confirm Dialog', async () => {
    mockedGetEmployeeDetail.mockResolvedValue(sampleEmployeeDetail);
    jest.spyOn(window, 'confirm').mockReturnValue(false);

    const { result } = renderHook(() => useADM003());

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
    });

    await act(async () => {
      await result.current.handleDelete();
    });

    expect(mockedDeleteEmployee).not.toHaveBeenCalled();
    expect(mockPush).not.toHaveBeenCalled();
  });

  it('handleDelete gọi API xóa thành công, lưu MSG003 và điều hướng sang ADM006', async () => {
    mockedGetEmployeeDetail.mockResolvedValue(sampleEmployeeDetail);
    mockedDeleteEmployee.mockResolvedValue({
      code: '200',
      employeeId: 10,
    });
    jest.spyOn(window, 'confirm').mockReturnValue(true);

    const { result } = renderHook(() => useADM003());

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
    });

    await act(async () => {
      await result.current.handleDelete();
    });

    expect(mockedDeleteEmployee).toHaveBeenCalledWith('10');
    expect(sessionStorage.getItem(STORAGE_KEYS.COMPLETE_MESSAGE_CODE)).toBe('MSG003');
    expect(mockPush).toHaveBeenCalledWith(ROUTES.EMPLOYEES.COMPLETE);
  });

  it('handleDelete hiển thị lỗi khi API xóa trả về thất bại', async () => {
    mockedGetEmployeeDetail.mockResolvedValue(sampleEmployeeDetail);
    mockedDeleteEmployee.mockRejectedValue({
      response: {
        data: {
          code: '500',
          message: {
            code: 'ER014',
            params: [],
          },
        },
      },
    });
    jest.spyOn(window, 'confirm').mockReturnValue(true);

    const { result } = renderHook(() => useADM003());

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
    });

    await act(async () => {
      await result.current.handleDelete();
    });

    expect(result.current.deleteError).toBe('該当するユーザは存在していません。');
    expect(mockPush).not.toHaveBeenCalledWith(ROUTES.EMPLOYEES.COMPLETE);
  });

  it('handleBack điều hướng quay lại màn hình danh sách ADM002', async () => {
    mockedGetEmployeeDetail.mockResolvedValue(sampleEmployeeDetail);

    const { result } = renderHook(() => useADM003());

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
    });

    act(() => {
      result.current.handleBack();
    });

    expect(mockPush).toHaveBeenCalledWith(ROUTES.EMPLOYEES.LIST);
  });

  it('chuyển hướng về ADM002 nếu employee có role là Admin (1)', async () => {
    mockedGetEmployeeDetail.mockResolvedValue({
      ...sampleEmployeeDetail,
      role: 1, // Admin role
    });

    renderHook(() => useADM003());

    await waitFor(() => {
      expect(mockReplace).toHaveBeenCalledWith(ROUTES.EMPLOYEES.LIST);
    });
  });

  it('hiển thị lỗi ER020 khi xóa tài khoản Admin', async () => {
    mockedGetEmployeeDetail.mockResolvedValue(sampleEmployeeDetail);
    mockedDeleteEmployee.mockRejectedValue({
      response: {
        data: {
          code: '500',
          message: {
            code: 'ER020',
            params: [],
          },
        },
      },
    });
    jest.spyOn(window, 'confirm').mockReturnValue(true);

    const { result } = renderHook(() => useADM003());

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
    });

    await act(async () => {
      await result.current.handleDelete();
    });

    expect(result.current.deleteError).toBe('管理者ユーザを削除することはできません。');
    expect(mockPush).not.toHaveBeenCalledWith(ROUTES.EMPLOYEES.COMPLETE);
  });
});

