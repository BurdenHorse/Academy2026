import { renderHook, act, waitFor } from '@testing-library/react';
import { useADM004 } from '@/hooks/useADM004';
import { useRouter, useSearchParams } from 'next/navigation';
import { employeeApi } from '@/lib/api/employee';
import { ROUTES, STORAGE_KEYS, MESSAGES } from '@/constants';

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
    get: mockSearchParamsGet,
  }),
}));

jest.mock('@/hooks/useDepartments', () => ({
  useDepartments: () => ({
    departments: [
      { departmentId: '1', departmentName: 'Phòng Phát Triển' },
      { departmentId: '2', departmentName: 'Phòng Kinh Doanh' },
    ],
    isLoadingDepartments: false,
    departmentError: null,
  }),
}));

jest.mock('@/hooks/useCertifications', () => ({
  useCertifications: () => ({
    certifications: [
      { certificationId: '1', certificationName: 'N1' },
      { certificationId: '2', certificationName: 'N2' },
    ],
    isLoadingCertifications: false,
    certificationError: null,
  }),
}));

jest.mock('@/lib/api/employee', () => ({
  employeeApi: {
    getEmployeeDetail: jest.fn(),
  },
}));

const mockedGetEmployeeDetail = employeeApi.getEmployeeDetail as jest.Mock;

describe('useADM004 Hook - Mode Add (No ID param)', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    sessionStorage.clear();
    mockSearchParamsGet.mockReturnValue(null); // Mode Add
  });

  it('initializes in Mode Add with empty formData and isEditMode false', () => {
    const { result } = renderHook(() => useADM004());

    expect(result.current.isEditMode).toBe(false);
    expect(result.current.isLoading).toBe(false);
    expect(result.current.formData.employeeLoginId).toBe('');
    expect(result.current.formData.employeeName).toBe('');
    expect(mockedGetEmployeeDetail).not.toHaveBeenCalled();
  });

  it('does NOT validate or set errors on handleInputChange (typing)', () => {
    const { result } = renderHook(() => useADM004());

    act(() => {
      // Type invalid employeeLoginId (starts with number, invalid format)
      result.current.handleInputChange('employeeLoginId', '1invalid');
    });

    expect(result.current.formData.employeeLoginId).toBe('1invalid');
    expect(result.current.errors.employeeLoginId).toBeUndefined();
  });

  it('validates and sets errors on handleInputBlur (out focus)', () => {
    const { result } = renderHook(() => useADM004());

    act(() => {
      result.current.handleInputChange('employeeLoginId', '1invalid');
    });
    expect(result.current.errors.employeeLoginId).toBeUndefined();

    act(() => {
      result.current.handleInputBlur('employeeLoginId', '1invalid');
    });

    expect(result.current.errors.employeeLoginId).toBeDefined();
    expect(result.current.errors.employeeLoginId).toContain('アカウント名');
  });

  it('clears error when field becomes valid and is blurred again', () => {
    const { result } = renderHook(() => useADM004());

    act(() => {
      result.current.handleInputChange('employeeEmail', 'invalid-email');
    });
    act(() => {
      result.current.handleInputBlur('employeeEmail', 'invalid-email');
    });
    expect(result.current.errors.employeeEmail).toBeDefined();

    act(() => {
      result.current.handleInputChange('employeeEmail', 'valid@example.com');
    });
    act(() => {
      result.current.handleInputBlur('employeeEmail', 'valid@example.com');
    });
    expect(result.current.errors.employeeEmail).toBeUndefined();
  });

  it('re-validates password confirmation on blur of employeeLoginPassword', () => {
    const { result } = renderHook(() => useADM004());

    act(() => {
      result.current.handleInputChange('employeeLoginPassword', 'Password123');
      result.current.handleInputChange('employeeLoginPasswordConfirm', 'Password123');
    });
    act(() => {
      result.current.handleInputBlur('employeeLoginPasswordConfirm', 'Password123');
    });
    expect(result.current.errors.employeeLoginPasswordConfirm).toBeUndefined();

    act(() => {
      result.current.handleInputChange('employeeLoginPassword', 'Different123');
    });
    act(() => {
      result.current.handleInputBlur('employeeLoginPassword', 'Different123');
    });

    expect(result.current.errors.employeeLoginPasswordConfirm).toBeDefined();
  });

  it('handleConfirm validates the full form and stops navigation if invalid', () => {
    const { result } = renderHook(() => useADM004());

    act(() => {
      result.current.handleConfirm();
    });

    expect(result.current.errors.employeeLoginId).toBeDefined();
    expect(result.current.errors.departmentId).toBeDefined();
    expect(result.current.errors.employeeName).toBeDefined();
    expect(result.current.errors.employeeLoginPassword).toBeDefined();
    expect(result.current.errors.employeeLoginPasswordConfirm).toBeDefined();
    expect(mockPush).not.toHaveBeenCalled();
  });

  it('handleBack in Mode Add navigates to employee list', () => {
    const { result } = renderHook(() => useADM004());

    act(() => {
      result.current.handleBack();
    });

    expect(mockPush).toHaveBeenCalledWith(ROUTES.EMPLOYEES.LIST);
  });

  it('loads serverError from sessionStorage and preserves formData', () => {
    sessionStorage.setItem('ADM004_SERVER_ERROR', '「アカウント名」は既に存在しています。');
    sessionStorage.setItem(
      'ADM004_FORM_DATA',
      JSON.stringify({
        employeeLoginId: 'admin_duplicate',
        departmentId: '1',
        employeeName: 'Nguyen Van A',
        employeeNameKana: 'グエン ヴァン アー',
        employeeBirthDate: '1995/05/20',
        employeeEmail: 'user@example.com',
        employeeTelephone: '0987654321',
        employeeLoginPassword: 'Password123',
        employeeLoginPasswordConfirm: 'Password123',
        certificationId: '',
        certificationStartDate: '',
        certificationEndDate: '',
        employeeCertificationScore: '',
      })
    );

    const { result } = renderHook(() => useADM004());

    expect(result.current.serverError).toBe('「アカウント名」は既に存在しています。');
    expect(result.current.formData.employeeLoginId).toBe('admin_duplicate');
    expect(result.current.formData.employeeName).toBe('Nguyen Van A');

    expect(sessionStorage.getItem('ADM004_SERVER_ERROR')).toBeNull();
    expect(sessionStorage.getItem('ADM004_FORM_DATA')).not.toBeNull();
  });

  it('redirects to /system-error if serverError in sessionStorage is system error', () => {
    sessionStorage.setItem('ADM004_SERVER_ERROR', MESSAGES.ERRORS.SYSTEM_ERROR);

    const { result } = renderHook(() => useADM004());

    expect(result.current.serverError).toBeNull();
    expect(mockReplace).toHaveBeenCalledWith(ROUTES.SYSTEM_ERROR);
    expect(sessionStorage.getItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE)).toBe(
      MESSAGES.ERRORS.SYSTEM_ERROR
    );
  });
});

describe('useADM004 Hook - Mode Edit (With ID param)', () => {
  const sampleEmployeeDetail = {
    code: '200',
    employeeId: 10,
    employeeName: 'Tran Van B',
    employeeBirthDate: '1990/01/01',
    departmentId: 1,
    departmentName: 'Phòng Phát Triển',
    employeeEmail: 'tranvanb@example.com',
    employeeTelephone: '0912345678',
    employeeNameKana: 'ﾄﾗﾝ ｳﾞｧﾝ ﾋﾞｰ',
    employeeLoginId: 'tranvanb',
    certifications: [
      {
        certificationId: 1,
        certificationName: 'N1',
        startDate: '2020/01/01',
        endDate: '2025/01/01',
        score: 150,
      },
    ],
  };

  beforeEach(() => {
    jest.clearAllMocks();
    sessionStorage.clear();
  });

  it('redirects to /system-error if ID is invalid (non-numeric or non-positive)', () => {
    mockSearchParamsGet.mockReturnValue('abc_invalid');

    renderHook(() => useADM004());

    expect(sessionStorage.getItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE)).toBe(
      MESSAGES.ERRORS.ER018('ＩＤ')
    );
    expect(mockReplace).toHaveBeenCalledWith(ROUTES.SYSTEM_ERROR);
    expect(mockedGetEmployeeDetail).not.toHaveBeenCalled();
  });

  it('fetches employee detail and populates formData on initial load', async () => {
    mockSearchParamsGet.mockReturnValue('10');
    mockedGetEmployeeDetail.mockResolvedValue(sampleEmployeeDetail);

    const { result } = renderHook(() => useADM004());

    expect(result.current.isEditMode).toBe(true);

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
    });

    expect(mockedGetEmployeeDetail).toHaveBeenCalledWith('10');
    expect(result.current.formData.employeeLoginId).toBe('tranvanb');
    expect(result.current.formData.employeeName).toBe('Tran Van B');
    expect(result.current.formData.departmentId).toBe('1');
    expect(result.current.formData.employeeEmail).toBe('tranvanb@example.com');
    expect(result.current.formData.certificationId).toBe('1');
    expect(result.current.formData.certificationStartDate).toBe('2020/01/01');
    expect(result.current.formData.certificationEndDate).toBe('2025/01/01');
    expect(result.current.formData.employeeCertificationScore).toBe('150');
    // Passwords must be empty in edit mode
    expect(result.current.formData.employeeLoginPassword).toBe('');
    expect(result.current.formData.employeeLoginPasswordConfirm).toBe('');
  });

  it('redirects to /system-error if getEmployeeDetail API fails', async () => {
    mockSearchParamsGet.mockReturnValue('10');
    mockedGetEmployeeDetail.mockRejectedValue({
      response: {
        data: {
          message: {
            code: 'ER013',
            params: [],
          },
        },
      },
    });

    renderHook(() => useADM004());

    await waitFor(() => {
      expect(mockReplace).toHaveBeenCalledWith(ROUTES.SYSTEM_ERROR);
    });
    expect(sessionStorage.getItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE)).toBeDefined();
  });

  it('does NOT fetch API if form data is already in sessionStorage (back from ADM005)', () => {
    mockSearchParamsGet.mockReturnValue('10');
    sessionStorage.setItem(
      STORAGE_KEYS.FORM_DATA,
      JSON.stringify({
        employeeLoginId: 'tranvanb_modified',
        departmentId: '2',
        employeeName: 'Tran Van B Modified',
        employeeNameKana: 'ﾄﾗﾝ ｳﾞｧﾝ ﾋﾞｰ',
        employeeBirthDate: '1990/01/01',
        employeeEmail: 'b_modified@example.com',
        employeeTelephone: '0912345678',
        employeeLoginPassword: '',
        employeeLoginPasswordConfirm: '',
        certificationId: '',
        certificationStartDate: '',
        certificationEndDate: '',
        employeeCertificationScore: '',
      })
    );

    const { result } = renderHook(() => useADM004());

    expect(result.current.isEditMode).toBe(true);
    expect(result.current.isLoading).toBe(false);
    expect(result.current.formData.employeeName).toBe('Tran Van B Modified');
    expect(mockedGetEmployeeDetail).not.toHaveBeenCalled();
  });

  it('validates password as optional in Mode Edit', async () => {
    mockSearchParamsGet.mockReturnValue('10');
    mockedGetEmployeeDetail.mockResolvedValue(sampleEmployeeDetail);

    const { result } = renderHook(() => useADM004());

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
    });

    // 1. When both password and confirm are empty: blur should produce NO errors
    act(() => {
      result.current.handleInputBlur('employeeLoginPassword', '');
      result.current.handleInputBlur('employeeLoginPasswordConfirm', '');
    });
    expect(result.current.errors.employeeLoginPassword).toBeUndefined();
    expect(result.current.errors.employeeLoginPasswordConfirm).toBeUndefined();

    // 2. When password is typed (< 8 chars)
    act(() => {
      result.current.handleInputChange('employeeLoginPassword', 'short');
      result.current.handleInputBlur('employeeLoginPassword', 'short');
    });
    expect(result.current.errors.employeeLoginPassword).toBeDefined();

    // 3. When password is valid length (8 chars) but confirm is empty
    act(() => {
      result.current.handleInputChange('employeeLoginPassword', 'Password123');
      result.current.handleInputBlur('employeeLoginPassword', 'Password123');
    });
    expect(result.current.errors.employeeLoginPassword).toBeUndefined();

    act(() => {
      result.current.handleInputBlur('employeeLoginPasswordConfirm', '');
    });
    expect(result.current.errors.employeeLoginPasswordConfirm).toBeDefined();

    // 4. When password and confirm match
    act(() => {
      result.current.handleInputChange('employeeLoginPasswordConfirm', 'Password123');
      result.current.handleInputBlur('employeeLoginPasswordConfirm', 'Password123');
    });
    expect(result.current.errors.employeeLoginPasswordConfirm).toBeUndefined();

    // 5. Submit with empty passwords passes validation
    await act(async () => {
      result.current.handleInputChange('employeeLoginPassword', '');
      result.current.handleInputChange('employeeLoginPasswordConfirm', '');
      await result.current.handleConfirm();
    });
    expect(mockPush).toHaveBeenCalledWith(`${ROUTES.EMPLOYEES.CONFIRM}?id=10`);
  });

  it('redirects to /system-error when employee does not exist on handleConfirm in Mode Edit', async () => {
    mockSearchParamsGet.mockReturnValue('10');
    mockedGetEmployeeDetail.mockResolvedValue(sampleEmployeeDetail);

    const { result } = renderHook(() => useADM004());

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
    });

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

    await act(async () => {
      await result.current.handleConfirm();
    });

    expect(mockReplace).toHaveBeenCalledWith(ROUTES.SYSTEM_ERROR);
    expect(sessionStorage.getItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE)).toBe(MESSAGES.ERRORS.ER013());
    expect(sessionStorage.getItem(STORAGE_KEYS.FORM_DATA)).toBeNull();
  });

  it('handleBack in Mode Edit navigates back to employee detail ADM003', async () => {
    mockSearchParamsGet.mockReturnValue('10');
    mockedGetEmployeeDetail.mockResolvedValue(sampleEmployeeDetail);

    const { result } = renderHook(() => useADM004());

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
    });

    act(() => {
      result.current.handleBack();
    });

    expect(mockPush).toHaveBeenCalledWith(`${ROUTES.EMPLOYEES.DETAIL}?id=10`);
  });

  it('restores original certification details when re-selecting original certification', async () => {
    mockSearchParamsGet.mockReturnValue('10');
    mockedGetEmployeeDetail.mockResolvedValue(sampleEmployeeDetail);

    const { result } = renderHook(() => useADM004());

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
    });

    expect(result.current.formData.certificationId).toBe('1');
    expect(result.current.formData.employeeCertificationScore).toBe('150');

    // 1. Change cert to empty (none selected) -> clears fields and disables
    act(() => {
      result.current.handleCertificationChange('');
    });

    expect(result.current.formData.certificationId).toBe('');
    expect(result.current.formData.certificationStartDate).toBe('');
    expect(result.current.formData.certificationEndDate).toBe('');
    expect(result.current.formData.employeeCertificationScore).toBe('');
    expect(result.current.isCertDisabled).toBe(true);

    // 2. Select another cert (ID '2') -> enabled but empty fields
    act(() => {
      result.current.handleCertificationChange('2');
    });
    expect(result.current.formData.certificationId).toBe('2');
    expect(result.current.formData.certificationStartDate).toBe('');

    // 3. Re-select original cert (ID '1') -> restores original cert data from API
    act(() => {
      result.current.handleCertificationChange('1');
    });
    expect(result.current.formData.certificationId).toBe('1');
    expect(result.current.formData.certificationStartDate).toBe('2020/01/01');
    expect(result.current.formData.certificationEndDate).toBe('2025/01/01');
    expect(result.current.formData.employeeCertificationScore).toBe('150');
  });
});
