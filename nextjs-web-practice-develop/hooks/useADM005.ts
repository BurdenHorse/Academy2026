import { useState, useEffect, useMemo } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import axios from 'axios';
import { ROUTES, STORAGE_KEYS, MESSAGES, formatErrorMessage, RESPONSE_CODES, MESSAGE_CODES, ERROR_CODES } from '@/constants';
import { EmployeeFormData, AddEmployeePayload, UpdateEmployeePayload } from '@/types/employee';
import { employeeApi } from '@/lib/api/employee';
import { useDepartments } from './useDepartments';
import { useCertifications } from './useCertifications';

/**
 * Custom hook quản lý dữ liệu và hành động cho màn hình Xác nhận thông tin (ADM005).
 *
 * @returns Object chứa formData, tên hiển thị phòng ban/chứng chỉ, trạng thái submit và handlers
 */
export function useADM005() {
  const router = useRouter();
  const searchParams = typeof useSearchParams === 'function' ? useSearchParams() : null;
  const id = searchParams ? searchParams.get('id') : null;
  const isEditMode = Boolean(id && id.trim() !== '');
  const returnToEditUrl = id ? `${ROUTES.EMPLOYEES.ADD_EDIT}?id=${id.trim()}` : ROUTES.EMPLOYEES.ADD_EDIT;

  // Danh mục phòng ban và chứng chỉ
  const { departments } = useDepartments();
  const { certifications } = useCertifications();

  // Dữ liệu từ form ADM004 lưu trong sessionStorage
  const [formData, setFormData] = useState<EmployeeFormData | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);

  useEffect(() => {
    if (typeof window !== 'undefined') {
      const savedFormDataJson = sessionStorage.getItem(STORAGE_KEYS.FORM_DATA);
      if (savedFormDataJson) {
        try {
          const parsedFormData = JSON.parse(savedFormDataJson);
          setFormData(parsedFormData);
        } catch {
          router.push(returnToEditUrl);
        }
      } else {
        router.push(returnToEditUrl);
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Tìm tên phòng ban tương ứng với departmentId đã chọn
  const departmentName = useMemo(() => {
    if (!formData?.departmentId) return '';
    const dept = departments.find(d => String(d.departmentId) === String(formData.departmentId));
    return dept ? dept.departmentName : '';
  }, [formData?.departmentId, departments]);

  // Tìm tên chứng chỉ tương ứng với certificationId đã chọn
  const certificationName = useMemo(() => {
    if (!formData?.certificationId) return '';
    const cert = certifications.find(c => String(c.certificationId) === String(formData.certificationId));
    return cert ? cert.certificationName : '';
  }, [formData?.certificationId, certifications]);

  /**
   * Xử lý xác nhận lưu thông tin vào DB qua API (POST hoặc PUT /employee) và chuyển tới trang hoàn thành (ADM006).
   */
  const handleSave = async () => {
    if (!formData || isSubmitting) return;

    try {
      setIsSubmitting(true);
      setSubmitError(null);

      const certs = formData.certificationId
        ? [
            {
              certificationId: Number(formData.certificationId),
              certificationStartDate: formData.certificationStartDate,
              certificationEndDate: formData.certificationEndDate,
              employeeCertificationScore: Number(formData.employeeCertificationScore),
            },
          ]
        : [];

      if (isEditMode) {
        // Kiểm tra chắc chắn nhân viên còn tồn tại trước khi cập nhật
        try {
          await employeeApi.getEmployeeDetail(id!.trim());
        } catch (checkErr: any) {
          const code = checkErr?.response?.data?.message?.code;
          const params = checkErr?.response?.data?.message?.params;
          const errMsg = formatErrorMessage(code, params) || MESSAGES.ERRORS.SYSTEM_ERROR;
          if (typeof window !== 'undefined') {
            sessionStorage.removeItem(STORAGE_KEYS.FORM_DATA);
            sessionStorage.removeItem(STORAGE_KEYS.SERVER_ERROR);
            sessionStorage.setItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE, errMsg);
          }
          router.replace(ROUTES.SYSTEM_ERROR);
          return;
        }

        const updatePayload: UpdateEmployeePayload = {
          employeeId: Number(id),
          employeeName: formData.employeeName,
          employeeBirthDate: formData.employeeBirthDate,
          employeeEmail: formData.employeeEmail,
          employeeTelephone: formData.employeeTelephone,
          employeeNameKana: formData.employeeNameKana,
          employeeLoginId: formData.employeeLoginId,
          departmentId: Number(formData.departmentId),
          certifications: certs,
          ...(formData.employeeLoginPassword ? { employeeLoginPassword: formData.employeeLoginPassword } : {}),
        };

        const updateEmployeeResponse = await employeeApi.updateEmployee(updatePayload);
        if (updateEmployeeResponse.code === RESPONSE_CODES.SUCCESS) {
          if (typeof window !== 'undefined') {
            sessionStorage.removeItem(STORAGE_KEYS.FORM_DATA);
            sessionStorage.removeItem(STORAGE_KEYS.SERVER_ERROR);
            sessionStorage.setItem(STORAGE_KEYS.COMPLETE_MESSAGE_CODE, MESSAGE_CODES.MSG002);
          }
          router.push(ROUTES.EMPLOYEES.COMPLETE);
        } else {
          if (updateEmployeeResponse.code === ERROR_CODES.ER013) {
            const errMsg = formatErrorMessage(updateEmployeeResponse.code, []);
            if (typeof window !== 'undefined') {
              sessionStorage.removeItem(STORAGE_KEYS.FORM_DATA);
              sessionStorage.removeItem(STORAGE_KEYS.SERVER_ERROR);
              sessionStorage.setItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE, errMsg);
            }
            router.replace(ROUTES.SYSTEM_ERROR);
            return;
          }
          const errorMsg = formatErrorMessage(updateEmployeeResponse.code, []);
          if (typeof window !== 'undefined') {
            sessionStorage.setItem(STORAGE_KEYS.SERVER_ERROR, errorMsg);
          }
          router.push(returnToEditUrl);
        }
      } else {
        const addPayload: AddEmployeePayload = {
          employeeName: formData.employeeName,
          employeeBirthDate: formData.employeeBirthDate,
          employeeEmail: formData.employeeEmail,
          employeeTelephone: formData.employeeTelephone,
          employeeNameKana: formData.employeeNameKana,
          employeeLoginId: formData.employeeLoginId,
          employeeLoginPassword: formData.employeeLoginPassword,
          departmentId: Number(formData.departmentId),
          certifications: certs,
        };

        const addEmployeeResponse = await employeeApi.addEmployee(addPayload);
        if (addEmployeeResponse.code === RESPONSE_CODES.SUCCESS) {
          if (typeof window !== 'undefined') {
            sessionStorage.removeItem(STORAGE_KEYS.FORM_DATA);
            sessionStorage.removeItem(STORAGE_KEYS.SERVER_ERROR);
            sessionStorage.setItem(STORAGE_KEYS.COMPLETE_MESSAGE_CODE, MESSAGE_CODES.MSG001);
          }
          router.push(ROUTES.EMPLOYEES.COMPLETE);
        } else {
          const errorMsg = formatErrorMessage(addEmployeeResponse.code, []);
          if (typeof window !== 'undefined') {
            sessionStorage.setItem(STORAGE_KEYS.SERVER_ERROR, errorMsg);
          }
          router.push(ROUTES.EMPLOYEES.ADD_EDIT);
        }
      }
    } catch (err: unknown) {
      let errorCode = '';
      let errorParams: (string | number)[] = [];

      if (axios.isAxiosError(err) && err.response?.data) {
        const data = err.response.data as {
          code?: string;
          message?: { code?: string; params?: (string | number)[] } | string;
        };
        if (data.message && typeof data.message === 'object') {
          errorCode = data.message.code || '';
          errorParams = Array.isArray(data.message.params) ? data.message.params : [];
        } else if (typeof data.message === 'string') {
          errorCode = data.message;
        } else if (data.code && data.code !== RESPONSE_CODES.SERVER_ERROR) {
          errorCode = data.code;
        }
      } else if (err && typeof err === 'object' && 'response' in err) {
        const anyErr = err as {
          response?: {
            data?: {
              code?: string;
              message?: { code?: string; params?: (string | number)[] } | string;
            };
          };
        };
        const data = anyErr.response?.data;
        if (data?.message && typeof data.message === 'object') {
          errorCode = data.message.code || '';
          errorParams = Array.isArray(data.message.params) ? data.message.params : [];
        } else if (typeof data?.message === 'string') {
          errorCode = data.message;
        } else if (data?.code && data.code !== RESPONSE_CODES.SERVER_ERROR) {
          errorCode = data.code;
        }
      }

      if (errorCode === ERROR_CODES.ER013) {
        const errorMsg = formatErrorMessage(errorCode, errorParams) || MESSAGES.ERRORS.ER013();
        if (typeof window !== 'undefined') {
          sessionStorage.removeItem(STORAGE_KEYS.FORM_DATA);
          sessionStorage.removeItem(STORAGE_KEYS.SERVER_ERROR);
          sessionStorage.setItem(STORAGE_KEYS.SYSTEM_ERROR_MESSAGE, errorMsg);
        }
        router.replace(ROUTES.SYSTEM_ERROR);
        return;
      }

      const errorMsg = formatErrorMessage(errorCode, errorParams);
      if (typeof window !== 'undefined') {
        sessionStorage.setItem(STORAGE_KEYS.SERVER_ERROR, errorMsg);
      }
      router.push(returnToEditUrl);
    } finally {
      setIsSubmitting(false);
    }
  };

  /**
   * Xử lý quay lại màn hình thêm mới / chỉnh sửa (ADM004) và giữ nguyên form data.
   */
  const handleBack = () => {
    router.push(returnToEditUrl);
  };

  return {
    formData,
    departmentName,
    certificationName,
    isSubmitting,
    submitError,
    isEditMode,
    handleSave,
    handleBack,
  };
}
