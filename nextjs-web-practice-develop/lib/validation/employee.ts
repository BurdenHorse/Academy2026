import { z } from 'zod';
import { EmployeeFormData } from '@/types/employee';
import { isValidCalendarDate } from '@/utils/date';
import { LABELS, MESSAGES } from '@/constants';

/**
 * Các biểu thức chính quy (Regex) kiểm tra định dạng
 */
export const REGEX_LOGIN_ID = /^[a-zA-Z_][a-zA-Z0-9_]*$/;
export const REGEX_KANA = /^[\uFF66-\uFF9F\s]+$/;
export const REGEX_EMAIL = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
export const REGEX_HALFSIZE_ASCII = /^[\x20-\x7E]+$/;
export const REGEX_HALFSIZE_TEL = /^[0-9+() -]+$/;
export const REGEX_HALFSIZE_NUM = /^[0-9]+$/;

/**
 * Zod Schema cho từng trường độc lập (dùng để validate realtime từng field khi người dùng nhập).
 */
export const singleFieldSchemas = {
  employeeLoginId: z
    .string()
    .trim()
    .min(1, MESSAGES.ERRORS.ER001(LABELS.FIELDS.ACCOUNT_NAME))
    .max(50, MESSAGES.ERRORS.ER006(LABELS.FIELDS.ACCOUNT_NAME, 50))
    .refine(val => REGEX_LOGIN_ID.test(val) && !/^[0-9]/.test(val), {
      message: MESSAGES.ERRORS.ER019(),
    }),

  departmentId: z
    .string()
    .trim()
    .min(1, MESSAGES.ERRORS.ER001(LABELS.FIELDS.GROUP)),

  employeeName: z
    .string()
    .trim()
    .min(1, MESSAGES.ERRORS.ER001(LABELS.FIELDS.FULL_NAME))
    .max(125, MESSAGES.ERRORS.ER006(LABELS.FIELDS.FULL_NAME, 125)),

  employeeNameKana: z
    .string()
    .trim()
    .min(1, MESSAGES.ERRORS.ER001(LABELS.FIELDS.FULL_NAME_KANA))
    .max(125, MESSAGES.ERRORS.ER006(LABELS.FIELDS.FULL_NAME_KANA, 125))
    .refine(val => REGEX_KANA.test(val), {
      message: MESSAGES.ERRORS.ER009(LABELS.FIELDS.FULL_NAME_KANA),
    }),

  employeeBirthDate: z
    .string()
    .trim()
    .min(1, MESSAGES.ERRORS.ER002(LABELS.FIELDS.BIRTH_DATE))
    .refine(val => isValidCalendarDate(val), {
      message: MESSAGES.ERRORS.ER011(LABELS.FIELDS.BIRTH_DATE),
    }),

  employeeEmail: z
    .string()
    .trim()
    .min(1, MESSAGES.ERRORS.ER001(LABELS.FIELDS.EMAIL))
    .max(125, MESSAGES.ERRORS.ER006(LABELS.FIELDS.EMAIL, 125))
    .refine(val => REGEX_HALFSIZE_ASCII.test(val), {
      message: MESSAGES.ERRORS.ER008(LABELS.FIELDS.EMAIL),
    })
    .refine(val => REGEX_EMAIL.test(val), {
      message: MESSAGES.ERRORS.ER005(LABELS.FIELDS.EMAIL, 'メール'),
    }),

  employeeTelephone: z
    .string()
    .trim()
    .min(1, MESSAGES.ERRORS.ER001(LABELS.FIELDS.TELEPHONE))
    .max(50, MESSAGES.ERRORS.ER006(LABELS.FIELDS.TELEPHONE, 50))
    .refine(val => REGEX_HALFSIZE_TEL.test(val), {
      message: MESSAGES.ERRORS.ER008(LABELS.FIELDS.TELEPHONE),
    }),

  employeeLoginPassword: z
    .string()
    .trim()
    .min(1, MESSAGES.ERRORS.ER001(LABELS.FIELDS.PASSWORD))
    .refine(val => val.length >= 8 && val.length <= 50, {
      message: MESSAGES.ERRORS.ER007(LABELS.FIELDS.PASSWORD, 8, 50),
    }),

  employeeLoginPasswordConfirm: z
    .string()
    .trim()
    .min(1, MESSAGES.ERRORS.ER001(LABELS.FIELDS.PASSWORD_CONFIRM)),

  certificationStartDate: z
    .string()
    .trim()
    .min(1, MESSAGES.ERRORS.ER002(LABELS.FIELDS.CERTIFICATION_START_DATE))
    .refine(val => isValidCalendarDate(val), {
      message: MESSAGES.ERRORS.ER011(LABELS.FIELDS.CERTIFICATION_START_DATE),
    }),

  certificationEndDate: z
    .string()
    .trim()
    .min(1, MESSAGES.ERRORS.ER002(LABELS.FIELDS.EXPIRATION_DATE))
    .refine(val => isValidCalendarDate(val), {
      message: MESSAGES.ERRORS.ER011(LABELS.FIELDS.EXPIRATION_DATE),
    }),

  employeeCertificationScore: z
    .string()
    .trim()
    .min(1, MESSAGES.ERRORS.ER001(LABELS.FIELDS.SCORE))
    .refine(val => REGEX_HALFSIZE_NUM.test(val) && parseInt(val, 10) >= 0, {
      message: MESSAGES.ERRORS.ER018(LABELS.FIELDS.SCORE),
    }),
};

/**
 * Helper validate các trường chứng chỉ (dùng chung cho cả Add Schema và Edit Schema).
 */
function validateCertificationFields(
  data: {
    certificationId?: string;
    certificationStartDate?: string;
    certificationEndDate?: string;
    employeeCertificationScore?: string;
  },
  ctx: z.RefinementCtx
) {
  if (data.certificationId && data.certificationId.trim() !== '') {
    // Validate ngày cấp
    const startDateVal = (data.certificationStartDate || '').trim();
    if (!startDateVal) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        path: ['certificationStartDate'],
        message: MESSAGES.ERRORS.ER002(LABELS.FIELDS.CERTIFICATION_START_DATE),
      });
    } else if (!isValidCalendarDate(startDateVal)) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        path: ['certificationStartDate'],
        message: MESSAGES.ERRORS.ER011(LABELS.FIELDS.CERTIFICATION_START_DATE),
      });
    }

    // Validate ngày hết hạn
    const endDateVal = (data.certificationEndDate || '').trim();
    if (!endDateVal) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        path: ['certificationEndDate'],
        message: MESSAGES.ERRORS.ER002(LABELS.FIELDS.EXPIRATION_DATE),
      });
    } else if (!isValidCalendarDate(endDateVal)) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        path: ['certificationEndDate'],
        message: MESSAGES.ERRORS.ER011(LABELS.FIELDS.EXPIRATION_DATE),
      });
    } else if (startDateVal && isValidCalendarDate(startDateVal)) {
      if (new Date(endDateVal) <= new Date(startDateVal)) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ['certificationEndDate'],
          message: MESSAGES.ERRORS.ER012(),
        });
      }
    }

    // Validate điểm số
    const scoreVal = (data.employeeCertificationScore || '').trim();
    if (!scoreVal) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        path: ['employeeCertificationScore'],
        message: MESSAGES.ERRORS.ER001(LABELS.FIELDS.SCORE),
      });
    } else if (!REGEX_HALFSIZE_NUM.test(scoreVal) || parseInt(scoreVal, 10) < 0) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        path: ['employeeCertificationScore'],
        message: MESSAGES.ERRORS.ER018(LABELS.FIELDS.SCORE),
      });
    }
  }
}

/**
 * Zod Schema đầy đủ cho Employee Form (Mode Add).
 */
export const employeeFormSchema = z
  .object({
    employeeLoginId: singleFieldSchemas.employeeLoginId,
    departmentId: singleFieldSchemas.departmentId,
    employeeName: singleFieldSchemas.employeeName,
    employeeNameKana: singleFieldSchemas.employeeNameKana,
    employeeBirthDate: singleFieldSchemas.employeeBirthDate,
    employeeEmail: singleFieldSchemas.employeeEmail,
    employeeTelephone: singleFieldSchemas.employeeTelephone,
    employeeLoginPassword: singleFieldSchemas.employeeLoginPassword,
    employeeLoginPasswordConfirm: singleFieldSchemas.employeeLoginPasswordConfirm,
    certificationId: z.string().optional(),
    certificationStartDate: z.string().optional(),
    certificationEndDate: z.string().optional(),
    employeeCertificationScore: z.string().optional(),
  })
  .superRefine((data, ctx) => {
    // 1. Kiểm tra mật khẩu xác nhận phải trùng khớp với mật khẩu (ER017)
    if (
      data.employeeLoginPassword &&
      data.employeeLoginPasswordConfirm &&
      data.employeeLoginPassword.trim() !== data.employeeLoginPasswordConfirm.trim()
    ) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        path: ['employeeLoginPasswordConfirm'],
        message: MESSAGES.ERRORS.ER017(),
      });
    }

    // 2. Nếu có chọn chứng chỉ -> kiểm tra bắt buộc và hợp lệ của 3 trường con
    validateCertificationFields(data, ctx);
  });

/**
 * Zod Schema đầy đủ cho Employee Form (Mode Edit - không bắt buộc nhập mật khẩu).
 */
export const employeeEditFormSchema = z
  .object({
    employeeLoginId: singleFieldSchemas.employeeLoginId,
    departmentId: singleFieldSchemas.departmentId,
    employeeName: singleFieldSchemas.employeeName,
    employeeNameKana: singleFieldSchemas.employeeNameKana,
    employeeBirthDate: singleFieldSchemas.employeeBirthDate,
    employeeEmail: singleFieldSchemas.employeeEmail,
    employeeTelephone: singleFieldSchemas.employeeTelephone,
    employeeLoginPassword: z.string().optional(),
    employeeLoginPasswordConfirm: z.string().optional(),
    certificationId: z.string().optional(),
    certificationStartDate: z.string().optional(),
    certificationEndDate: z.string().optional(),
    employeeCertificationScore: z.string().optional(),
  })
  .superRefine((data, ctx) => {
    const password = (data.employeeLoginPassword || '').trim();
    const confirm = (data.employeeLoginPasswordConfirm || '').trim();

    // 1. Validate password & confirm password ở Mode Edit
    if (password) {
      if (password.length < 8 || password.length > 50) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ['employeeLoginPassword'],
          message: MESSAGES.ERRORS.ER007(LABELS.FIELDS.PASSWORD, 8, 50),
        });
      }
      if (!confirm) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ['employeeLoginPasswordConfirm'],
          message: MESSAGES.ERRORS.ER001(LABELS.FIELDS.PASSWORD_CONFIRM),
        });
      } else if (password !== confirm) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ['employeeLoginPasswordConfirm'],
          message: MESSAGES.ERRORS.ER017(),
        });
      }
    } else if (confirm) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        path: ['employeeLoginPasswordConfirm'],
        message: MESSAGES.ERRORS.ER017(),
      });
    }

    // 2. Nếu có chọn chứng chỉ -> kiểm tra bắt buộc và hợp lệ của 3 trường con
    validateCertificationFields(data, ctx);
  });

/**
 * Validate một trường đơn lẻ realtime bằng Zod Schema.
 *
 * @param field Tên trường
 * @param value Giá trị trường
 * @param allData Toàn bộ dữ liệu form hiện tại
 * @param isEditMode Chế độ Edit (mặc định false - Mode Add)
 * @returns string Thông báo lỗi (hoặc rỗng nếu hợp lệ)
 */
export function validateEmployeeField(
  field: keyof EmployeeFormData,
  value: string,
  allData: EmployeeFormData,
  isEditMode: boolean = false
): string {
  const trimmed = value.trim();

  // Xử lý riêng cho Mode Edit đối với mật khẩu
  if (isEditMode) {
    if (field === 'employeeLoginPassword') {
      if (!trimmed) return '';
      if (trimmed.length < 8 || trimmed.length > 50) {
        return MESSAGES.ERRORS.ER007(LABELS.FIELDS.PASSWORD, 8, 50);
      }
      return '';
    }

    if (field === 'employeeLoginPasswordConfirm') {
      const password = (allData.employeeLoginPassword || '').trim();
      if (!password && !trimmed) return '';
      if (password && !trimmed) {
        return MESSAGES.ERRORS.ER001(LABELS.FIELDS.PASSWORD_CONFIRM);
      }
      if (trimmed !== password) {
        return MESSAGES.ERRORS.ER017();
      }
      return '';
    }
  }

  // 1. Nếu trường có schema độc lập trong singleFieldSchemas
  if (field in singleFieldSchemas) {
    // Với các trường chứng chỉ: nếu không chọn certificationId thì bỏ qua
    if (
      (field === 'certificationStartDate' ||
        field === 'certificationEndDate' ||
        field === 'employeeCertificationScore') &&
      (!allData.certificationId || allData.certificationId.trim() === '')
    ) {
      return '';
    }

    const schema = singleFieldSchemas[field as keyof typeof singleFieldSchemas];
    const result = schema.safeParse(trimmed);
    if (!result.success) {
      return result.error.issues[0]?.message || '';
    }
  }

  // 2. Kiểm tra các logic tương tác giữa các trường
  if (field === 'employeeLoginPasswordConfirm') {
    if (trimmed && allData.employeeLoginPassword.trim() && trimmed !== allData.employeeLoginPassword.trim()) {
      return MESSAGES.ERRORS.ER017();
    }
  }

  if (field === 'certificationEndDate' && allData.certificationId) {
    if (
      allData.certificationStartDate &&
      isValidCalendarDate(allData.certificationStartDate) &&
      isValidCalendarDate(trimmed)
    ) {
      if (new Date(trimmed) <= new Date(allData.certificationStartDate)) {
        return MESSAGES.ERRORS.ER012();
      }
    }
  }

  return '';
}

/**
 * Validate toàn bộ form Employee bằng Zod Schema.
 *
 * @param formData Dữ liệu form cần kiểm tra
 * @param isEditMode Chế độ Edit (mặc định false - Mode Add)
 * @returns Record<string, string> Danh sách lỗi theo từng field name (rỗng nếu form hợp lệ)
 */
export function validateEmployeeForm(
  formData: EmployeeFormData,
  isEditMode: boolean = false
): Record<string, string> {
  const schema = isEditMode ? employeeEditFormSchema : employeeFormSchema;
  const result = schema.safeParse(formData);
  const errors: Record<string, string> = {};

  if (!result.success) {
    for (const issue of result.error.issues) {
      const fieldName = issue.path[0] as string;
      if (fieldName && !errors[fieldName]) {
        errors[fieldName] = issue.message;
      }
    }
  }

  return errors;
}
