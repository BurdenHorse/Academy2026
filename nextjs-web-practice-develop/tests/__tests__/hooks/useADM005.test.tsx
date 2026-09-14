import { renderHook, act } from '@testing-library/react';
import { useADM005 } from '@/hooks/useADM005';
import { employeeApi } from '@/lib/api/employee';
import { useRouter, useSearchParams } from 'next/navigation';
import { STORAGE_KEYS, ROUTES } from '@/constants';

const mockPush = jest.fn();
const mockReplace = jest.fn();
const mockRouter = {
  push: mockPush,
  replace: mockReplace,
};
let mockSearchParamsGet = jest.fn();

jest.mock('next/navigation', () => ({
  useRouter: () => mockRouter,
  useSearchParams: () => ({
    get: (param: string) => mockSearchParamsGet(param),
  }),
}));

jest.mock('@/lib/api/employee', () => ({
  employeeApi: {
    addEmployee: jest.fn(),
    updateEmployee: jest.fn(),
    getEmployeeDetail: jest.fn(),
  },
}));

jest.mock('@/hooks/useDepartments', () => ({
  useDepartments: () => ({
    departments: [{ departmentId: '1', departmentName: 'Phòng Phát Triển' }],
  }),
}));

jest.mock('@/hooks/useCertifications', () => ({
  useCertifications: () => ({
    certifications: [{ certificationId: '1', certificationName: 'N1' }],
  }),
}));

const mockedAddEmployee = employeeApi.addEmployee as jest.Mock;
const mockedUpdateEmployee = employeeApi.updateEmployee as jest.Mock;
const mockedGetEmployeeDetail = employeeApi.getEmployeeDetail as jest.Mock;

describe('useADM005 Hook', () => {
  const sampleFormData = {
    employeeLoginId: 'duplicate_user',
    departmentId: '1',
    employeeName: 'Nguyen Van B',
    employeeNameKana: 'グエン ヴァン ビー',
    employeeBirthDate: '1995/05/20',
    employeeEmail: 'user@example.com',
    employeeTelephone: '0987654321',
    employeeLoginPassword: 'Password123',
    employeeLoginPasswordConfirm: 'Password123',
    certificationId: '1',
    certificationStartDate: '2024/01/01',
    certificationEndDate: '2025/01/01',
    employeeCertificationScore: '150',
  };

  beforeEach(() => {
    jest.clearAllMocks();
    sessionStorage.clear();
    sessionStorage.setItem(STORAGE_KEYS.FORM_DATA, JSON.stringify(sampleFormData));
    mockSearchParamsGet.mockReturnValue(null); // default to Mode Add
  });

  describe('Mode Add', () => {
    it('redirects to ADM004 and stores formatted error message when backend returns ER003', async () => {
      mockedAddEmployee.mockRejectedValue({
        isAxiosError: true,
        response: {
          status: 500,
          data: {
            code: '500',
            message: {
              code: 'ER003',
              params: ['アカウント名'],
            },
          },
        },
      });

      const { result } = renderHook(() => useADM005());

      await act(async () => {
        await result.current.handleSave();
      });

      expect(mockPush).toHaveBeenCalledWith(ROUTES.EMPLOYEES.ADD_EDIT);
      expect(sessionStorage.getItem(STORAGE_KEYS.FORM_DATA)).toBe(JSON.stringify(sampleFormData));
      expect(sessionStorage.getItem(STORAGE_KEYS.SERVER_ERROR)).toBe('「アカウント名」は既に存在しています。');
    });

    it('redirects to ADM004 when backend returns non-200 or ER023', async () => {
      mockedAddEmployee.mockRejectedValue({
        isAxiosError: true,
        response: {
          status: 500,
          data: {
            code: '500',
            message: {
              code: 'ER023',
              params: [],
            },
          },
        },
      });

      const { result } = renderHook(() => useADM005());

      await act(async () => {
        await result.current.handleSave();
      });

      expect(mockPush).toHaveBeenCalledWith(ROUTES.EMPLOYEES.ADD_EDIT);
      expect(sessionStorage.getItem(STORAGE_KEYS.SERVER_ERROR)).toBe('システムエラーが発生しました。');
      expect(sessionStorage.getItem(STORAGE_KEYS.FORM_DATA)).toBe(JSON.stringify(sampleFormData));
    });

    it('redirects to ADM006, clears form data, and sets MSG001 when backend succeeds with 200', async () => {
      mockedAddEmployee.mockResolvedValue({
        code: '200',
        employeeId: 10,
      });

      const { result } = renderHook(() => useADM005());

      await act(async () => {
        await result.current.handleSave();
      });

      expect(mockPush).toHaveBeenCalledWith(ROUTES.EMPLOYEES.COMPLETE);
      expect(sessionStorage.getItem(STORAGE_KEYS.FORM_DATA)).toBeNull();
      expect(sessionStorage.getItem(STORAGE_KEYS.SERVER_ERROR)).toBeNull();
      expect(sessionStorage.getItem(STORAGE_KEYS.COMPLETE_MESSAGE_CODE)).toBe('MSG001');
    });

    it('navigates back to ADM004 on handleBack', () => {
      const { result } = renderHook(() => useADM005());

      act(() => {
        result.current.handleBack();
      });

      expect(mockPush).toHaveBeenCalledWith(ROUTES.EMPLOYEES.ADD_EDIT);
    });
  });

  describe('Mode Edit', () => {
    beforeEach(() => {
      mockSearchParamsGet.mockImplementation((param: string) => {
        if (param === 'id') return '15';
        return null;
      });
      mockedGetEmployeeDetail.mockResolvedValue({
        employeeId: 15,
        employeeLoginId: 'duplicate_user',
      });
    });

    it('calls updateEmployee with employeeId, sets MSG002 and redirects to ADM006 on success', async () => {
      mockedUpdateEmployee.mockResolvedValue({
        code: '200',
        employeeId: 15,
      });

      const { result } = renderHook(() => useADM005());

      expect(result.current.isEditMode).toBe(true);

      await act(async () => {
        await result.current.handleSave();
      });

      expect(mockedGetEmployeeDetail).toHaveBeenCalledWith('15');
      expect(mockedUpdateEmployee).toHaveBeenCalledWith(
        expect.objectContaining({
          employeeId: 15,
          employeeLoginId: 'duplicate_user',
          departmentId: 1,
        })
      );
      expect(mockPush).toHaveBeenCalledWith(ROUTES.EMPLOYEES.COMPLETE);
      expect(sessionStorage.getItem(STORAGE_KEYS.COMPLETE_MESSAGE_CODE)).toBe('MSG002');
      expect(sessionStorage.getItem(STORAGE_KEYS.FORM_DATA)).toBeNull();
    });

    it('redirects to /system-error when employee does not exist before update in Mode Edit', async () => {
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

      const { result } = renderHook(() => useADM005());

      await act(async () => {
        await result.current.handleSave();
      });

      expect(mockReplace).toHaveBeenCalledWith(ROUTES.SYSTEM_ERROR);
      expect(sessionStorage.getItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE)).toBe('該当するユーザは存在していません。');
      expect(sessionStorage.getItem(STORAGE_KEYS.FORM_DATA)).toBeNull();
      expect(mockedUpdateEmployee).not.toHaveBeenCalled();
    });

    it('redirects to /system-error when updateEmployee returns ER013', async () => {
      mockedGetEmployeeDetail.mockResolvedValue({ employeeId: 15 });
      mockedUpdateEmployee.mockRejectedValue({
        isAxiosError: true,
        response: {
          status: 500,
          data: {
            code: '500',
            message: {
              code: 'ER013',
              params: [],
            },
          },
        },
      });

      const { result } = renderHook(() => useADM005());

      await act(async () => {
        await result.current.handleSave();
      });

      expect(mockReplace).toHaveBeenCalledWith(ROUTES.SYSTEM_ERROR);
      expect(sessionStorage.getItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE)).toBe('該当するユーザは存在していません。');
      expect(sessionStorage.getItem(STORAGE_KEYS.FORM_DATA)).toBeNull();
    });

    it('redirects to ADM004 with id param on update failure', async () => {
      mockedUpdateEmployee.mockRejectedValue({
        isAxiosError: true,
        response: {
          status: 500,
          data: {
            code: '500',
            message: {
              code: 'ER003',
              params: ['アカウント名'],
            },
          },
        },
      });

      const { result } = renderHook(() => useADM005());

      await act(async () => {
        await result.current.handleSave();
      });

      expect(mockPush).toHaveBeenCalledWith(`${ROUTES.EMPLOYEES.ADD_EDIT}?id=15`);
      expect(sessionStorage.getItem(STORAGE_KEYS.SERVER_ERROR)).toBe('「アカウント名」は既に存在しています。');
    });

    it('navigates back to ADM004 with id param on handleBack', () => {
      const { result } = renderHook(() => useADM005());

      act(() => {
        result.current.handleBack();
      });

      expect(mockPush).toHaveBeenCalledWith(`${ROUTES.EMPLOYEES.ADD_EDIT}?id=15`);
    });
  });
});
