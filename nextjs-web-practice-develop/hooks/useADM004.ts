import { useState, useRef, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import DatePicker from 'react-datepicker';
import { ROUTES, STORAGE_KEYS, MESSAGES, formatErrorMessage } from '@/constants';
import { EmployeeFormData } from '@/types/employee';
import { employeeApi } from '@/lib/api/employee';
import { formatDateToString, parseStringToDate, isValidCalendarDate } from '@/utils/date';
import { validateEmployeeField, validateEmployeeForm } from '@/lib/validation';
import { useDepartments } from './useDepartments';
import { useCertifications } from './useCertifications';

export { formatDateToString, parseStringToDate, isValidCalendarDate };

/**
 * Custom hook quản lý toàn bộ Form State và Hành động cho màn hình ADM004 (Mode Add và Mode Edit).
 * Phân biệt Mode dựa trên tham số query `id` trên URL.
 * Sử dụng Zod Schema để validate realtime khi out focus và validate khi submit.
 */
export function useADM004() {
  const router = useRouter();
  const searchParams = typeof useSearchParams === 'function' ? useSearchParams() : null;
  const id = searchParams ? searchParams.get('id') : null;

  // Xác định chế độ: có ID là Edit, không có ID là Add
  const isEditMode = Boolean(id);
  const trimmedId = id ? id.trim() : '';
  const isValidId = trimmedId !== '' && /^\d+$/.test(trimmedId) && parseInt(trimmedId, 10) > 0;

  // Tái sử dụng hooks danh mục phòng ban và chứng chỉ
  const { departments, isLoadingDepartments, departmentError } = useDepartments();
  const { certifications, isLoadingCertifications, certificationError } = useCertifications();

  // Form State (khôi phục từ sessionStorage nếu người dùng quay lại từ ADM005)
  const [formData, setFormData] = useState<EmployeeFormData>(() => {
    if (typeof window !== 'undefined') {
      const saved = sessionStorage.getItem(STORAGE_KEYS.FORM_DATA);
      if (saved) {
        try {
          return JSON.parse(saved);
        } catch {
          // ignore
        }
      }
    }
    return {
      employeeLoginId: '',
      departmentId: '',
      employeeName: '',
      employeeNameKana: '',
      employeeBirthDate: '',
      employeeEmail: '',
      employeeTelephone: '',
      employeeLoginPassword: '',
      employeeLoginPasswordConfirm: '',
      certificationId: '',
      certificationStartDate: '',
      certificationEndDate: '',
      employeeCertificationScore: '',
    };
  });

  // State lưu trữ thông báo lỗi của từng trường
  const [errors, setErrors] = useState<Record<string, string>>({});

  // Trạng thái loading khi fetch chi tiết nhân viên ở Mode Edit
  const [isLoading, setIsLoading] = useState<boolean>(() => {
    if (typeof window !== 'undefined' && isEditMode) {
      const saved = sessionStorage.getItem(STORAGE_KEYS.FORM_DATA);
      if (saved) return false;
      return true;
    }
    return false;
  });

  // State lưu trữ thông báo lỗi từ server (ví dụ: ER003 khi redirect từ ADM005 về)
  const [serverError, setServerError] = useState<string | null>(() => {
    if (typeof window !== 'undefined') {
      const err = sessionStorage.getItem(STORAGE_KEYS.SERVER_ERROR);
      if (err) {
        sessionStorage.removeItem(STORAGE_KEYS.SERVER_ERROR);
        if (
          err === MESSAGES.ERRORS.SYSTEM_ERROR ||
          err === MESSAGES.ERRORS.ER015() ||
          err === MESSAGES.ERRORS.ER023()
        ) {
          sessionStorage.setItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE, MESSAGES.ERRORS.SYSTEM_ERROR);
          return null;
        }
        return err;
      }
    }
    return null;
  });

  // Trạng thái đang submit kiểm tra dữ liệu trước khi chuyển trang
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  // Lưu thông tin chứng chỉ ban đầu từ API (ở Mode Edit) để khôi phục khi chọn lại
  const originalCertRef = useRef<{
    certificationId: string;
    certificationStartDate: string;
    certificationEndDate: string;
    employeeCertificationScore: string;
  } | null>(null);

  // Refs cho DatePicker và input đầu tiên (Auto Focus)
  const accountNameRef = useRef<HTMLInputElement>(null);
  const birthDateRef = useRef<DatePicker>(null);
  const certificationStartDateRef = useRef<DatePicker>(null);
  const certificationEndDateRef = useRef<DatePicker>(null);

  // Kiểm tra nếu có thông báo lỗi hệ thống cần redirect
  useEffect(() => {
    if (typeof window !== 'undefined') {
      const sysMsg = sessionStorage.getItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE);
      if (sysMsg) {
        router.replace(ROUTES.SYSTEM_ERROR);
        return;
      }
    }

    if (departmentError || certificationError) {
      sessionStorage.setItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE, MESSAGES.ERRORS.SYSTEM_ERROR);
      router.replace(ROUTES.SYSTEM_ERROR);
      return;
    }
  }, [departmentError, certificationError, router]);

  // Khởi tạo dữ liệu màn hình (Add hoặc Edit)
  useEffect(() => {
    // 1. Nếu là Mode Edit nhưng ID không hợp lệ (không phải số nguyên dương) -> sang /system-error
    if (isEditMode && !isValidId) {
      sessionStorage.setItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE, MESSAGES.ERRORS.ER018('ＩＤ'));
      router.replace(ROUTES.SYSTEM_ERROR);
      return;
    }

    // 2. Nếu đã có formData trong sessionStorage (quay lại từ ADM005) -> không cần fetch lại API
    if (typeof window !== 'undefined') {
      const saved = sessionStorage.getItem(STORAGE_KEYS.FORM_DATA);
      if (saved) {
        setIsLoading(false);
        return;
      }
    }

    // 3. Nếu là Mode Add (không có id): focus vào ô Account Name
    if (!isEditMode) {
      accountNameRef.current?.focus();
      return;
    }

    // 4. Nếu là Mode Edit và chưa có form data -> fetch chi tiết nhân viên từ API
    let isMounted = true;
    setIsLoading(true);

    employeeApi
      .getEmployeeDetail(trimmedId)
      .then((employeeDetailData) => {
        if (!isMounted) return;

        const primaryCert =
          employeeDetailData.certifications && employeeDetailData.certifications.length > 0
            ? employeeDetailData.certifications[0]
            : null;

        const certInfo = primaryCert
          ? {
              certificationId: String(primaryCert.certificationId),
              certificationStartDate: primaryCert.startDate || '',
              certificationEndDate: primaryCert.endDate || '',
              employeeCertificationScore:
                primaryCert.score !== undefined && primaryCert.score !== null
                  ? String(primaryCert.score)
                  : '',
            }
          : {
              certificationId: '',
              certificationStartDate: '',
              certificationEndDate: '',
              employeeCertificationScore: '',
            };

        originalCertRef.current = primaryCert ? { ...certInfo } : null;

        setFormData({
          employeeLoginId: employeeDetailData.employeeLoginId || '',
          departmentId: employeeDetailData.departmentId
            ? String(employeeDetailData.departmentId)
            : '',
          employeeName: employeeDetailData.employeeName || '',
          employeeNameKana: employeeDetailData.employeeNameKana || '',
          employeeBirthDate: employeeDetailData.employeeBirthDate || '',
          employeeEmail: employeeDetailData.employeeEmail || '',
          employeeTelephone: employeeDetailData.employeeTelephone || '',
          employeeLoginPassword: '',
          employeeLoginPasswordConfirm: '',
          ...certInfo,
        });
        setIsLoading(false);
      })
      .catch((apiError) => {
        if (!isMounted) return;
        setIsLoading(false);
        const code = apiError?.response?.data?.message?.code;
        const params = apiError?.response?.data?.message?.params;
        const errMsg = formatErrorMessage(code, params) || MESSAGES.ERRORS.SYSTEM_ERROR;
        sessionStorage.setItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE, errMsg);
        router.replace(ROUTES.SYSTEM_ERROR);
      });

    return () => {
      isMounted = false;
    };
  }, [isEditMode, isValidId, trimmedId, router]);

  // Xác định trạng thái disable của cụm chứng chỉ
  const isCertDisabled = !formData.certificationId;

  /**
   * Xử lý khi thay đổi giá trị của bất kỳ input text/select nào.
   * Chỉ cập nhật state formData mà không thực hiện validate realtime.
   */
  const handleInputChange = (field: keyof EmployeeFormData, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  /**
   * Xử lý khi input mất focus (onBlur / out focus).
   * Thực hiện validate trường vừa out focus bằng Zod Schema.
   */
  const handleInputBlur = (field: keyof EmployeeFormData, currentValue?: string) => {
    const val = currentValue !== undefined ? currentValue : formData[field] || '';
    const currentFormData = { ...formData, [field]: val };
    const errorMsg = validateEmployeeField(field, val, currentFormData, isEditMode);

    setErrors((prev) => {
      const next = { ...prev };
      if (errorMsg) {
        next[field] = errorMsg;
      } else {
        delete next[field];
      }

      // Nếu out focus khỏi employeeLoginPassword và đã có employeeLoginPasswordConfirm, kiểm tra lại confirm password
      if (field === 'employeeLoginPassword' && currentFormData.employeeLoginPasswordConfirm) {
        const confirmErr = validateEmployeeField(
          'employeeLoginPasswordConfirm',
          currentFormData.employeeLoginPasswordConfirm,
          currentFormData,
          isEditMode
        );
        if (confirmErr) {
          next.employeeLoginPasswordConfirm = confirmErr;
        } else {
          delete next.employeeLoginPasswordConfirm;
        }
      }

      // Nếu out focus khỏi certificationStartDate và đã có certificationEndDate, kiểm tra lại certificationEndDate
      if (field === 'certificationStartDate' && currentFormData.certificationEndDate) {
        const endErr = validateEmployeeField(
          'certificationEndDate',
          currentFormData.certificationEndDate,
          currentFormData,
          isEditMode
        );
        if (endErr) {
          next.certificationEndDate = endErr;
        } else {
          delete next.certificationEndDate;
        }
      }

      return next;
    });
  };

  /**
   * Xử lý khi chọn dropdown Chứng chỉ tiếng Nhật.
   */
  const handleCertificationChange = (certId: string) => {
    if (!certId) {
      // Khi chọn về rỗng: clear data, disable, xóa bỏ lỗi cụm chứng chỉ
      setFormData((prev) => ({
        ...prev,
        certificationId: '',
        certificationStartDate: '',
        certificationEndDate: '',
        employeeCertificationScore: '',
      }));
      setErrors((prev) => {
        const copy = { ...prev };
        delete copy.certificationStartDate;
        delete copy.certificationEndDate;
        delete copy.employeeCertificationScore;
        return copy;
      });
    } else {
      // Khi chọn từ rỗng sang có giá trị
      if (
        isEditMode &&
        originalCertRef.current &&
        originalCertRef.current.certificationId === certId
      ) {
        // Mode edit: khôi phục giá trị ban đầu từ API nếu trùng chứng chỉ ban đầu
        setFormData((prev) => ({
          ...prev,
          certificationId: certId,
          certificationStartDate: originalCertRef.current!.certificationStartDate,
          certificationEndDate: originalCertRef.current!.certificationEndDate,
          employeeCertificationScore: originalCertRef.current!.employeeCertificationScore,
        }));
      } else {
        setFormData((prev) => ({ ...prev, certificationId: certId }));
      }
    }
  };

  /**
   * Xử lý khi chọn Date từ DatePicker.
   */
  const handleDateChange = (
    field: 'employeeBirthDate' | 'certificationStartDate' | 'certificationEndDate',
    date: Date | null
  ) => {
    const dateStr = formatDateToString(date);
    handleInputChange(field, dateStr);
  };

  /**
   * Xử lý khi bấm nút "確認" (Confirm / Xác nhận).
   * Validate toàn bộ form bằng Zod Schema (tùy theo Mode Add hay Mode Edit).
   * Ở Mode Edit: kiểm tra nhân viên có tồn tại qua getEmployeeDetail trước khi chuyển sang ADM005.
   */
  const handleConfirm = async () => {
    if (isSubmitting) return;

    setServerError(null);
    const validationErrors = validateEmployeeForm(formData, isEditMode);
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    if (isEditMode) {
      try {
        setIsSubmitting(true);
        await employeeApi.getEmployeeDetail(trimmedId);
      } catch (apiError: any) {
        setIsSubmitting(false);
        const code = apiError?.response?.data?.message?.code;
        const params = apiError?.response?.data?.message?.params;
        const errMsg = formatErrorMessage(code, params) || MESSAGES.ERRORS.SYSTEM_ERROR;
        if (typeof window !== 'undefined') {
          sessionStorage.removeItem(STORAGE_KEYS.FORM_DATA);
          sessionStorage.setItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE, errMsg);
        }
        router.replace(ROUTES.SYSTEM_ERROR);
        return;
      } finally {
        setIsSubmitting(false);
      }
    }

    // Lưu formData vào sessionStorage để ADM005 hiển thị và khôi phục khi Back
    if (typeof window !== 'undefined') {
      sessionStorage.setItem(STORAGE_KEYS.FORM_DATA, JSON.stringify(formData));
    }

    if (isEditMode) {
      router.push(`${ROUTES.EMPLOYEES.CONFIRM}?id=${trimmedId}`);
    } else {
      router.push(ROUTES.EMPLOYEES.CONFIRM);
    }
  };

  /**
   * Xử lý khi bấm nút "キャンセル" (Cancel / Quay lại).
   * Mode Add: Quay về danh sách nhân viên (ADM002).
   * Mode Edit: Quay về màn hình chi tiết nhân viên (ADM003) kèm ID.
   */
  const handleBack = () => {
    if (typeof window !== 'undefined') {
      sessionStorage.removeItem(STORAGE_KEYS.FORM_DATA);
      sessionStorage.removeItem(STORAGE_KEYS.SERVER_ERROR);
    }

    if (isEditMode) {
      router.push(`${ROUTES.EMPLOYEES.DETAIL}?id=${trimmedId}`);
    } else {
      router.push(ROUTES.EMPLOYEES.LIST);
    }
  };

  return {
    departments,
    isLoadingDepartments,
    departmentError,
    certifications,
    isLoadingCertifications,
    certificationError,
    formData,
    errors,
    serverError,
    isLoading,
    isSubmitting,
    isEditMode,
    isCertDisabled,
    accountNameRef,
    birthDateRef,
    certificationStartDateRef,
    certificationEndDateRef,
    handleInputChange,
    handleInputBlur,
    handleCertificationChange,
    handleDateChange,
    handleConfirm,
    handleBack,
  };
}
