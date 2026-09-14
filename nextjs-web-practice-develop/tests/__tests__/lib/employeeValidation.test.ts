import {
  validateEmployeeField,
  validateEmployeeForm,
} from '@/lib/validation/employee';
import { isValidCalendarDate, formatDateToString, parseStringToDate } from '@/utils/date';
import { EmployeeFormData } from '@/types/employee';
import { LABELS, MESSAGES } from '@/constants';

describe('Date Utilities', () => {
  it('correctly validates calendar dates', () => {
    expect(isValidCalendarDate('2023/02/28')).toBe(true);
    expect(isValidCalendarDate('2024/02/29')).toBe(true); // Năm nhuận
    expect(isValidCalendarDate('2023/02/29')).toBe(false); // Năm không nhuận
    expect(isValidCalendarDate('2023/13/01')).toBe(false);
    expect(isValidCalendarDate('invalid-date')).toBe(false);
  });

  it('formats Date object to yyyy/MM/dd string', () => {
    const date = new Date(2023, 6, 8); // month is 0-indexed (6 = July)
    expect(formatDateToString(date)).toBe('2023/07/08');
  });

  it('parses yyyy/MM/dd string to Date object', () => {
    const date = parseStringToDate('2023/07/08');
    expect(date?.getFullYear()).toBe(2023);
    expect(date?.getMonth()).toBe(6);
    expect(date?.getDate()).toBe(8);
  });
});

describe('Employee Zod Validation', () => {
  const validData: EmployeeFormData = {
    employeeLoginId: 'admin_01',
    departmentId: '1',
    employeeName: 'Nguyen Van A',
    employeeNameKana: 'ｸﾞｴﾝ ｳﾞｧﾝ ｴｰ',
    employeeBirthDate: '1990/01/01',
    employeeEmail: 'admin@luvina.net',
    employeeTelephone: '0912345678',
    employeeLoginPassword: 'password123',
    employeeLoginPasswordConfirm: 'password123',
    certificationId: '',
    certificationStartDate: '',
    certificationEndDate: '',
    employeeCertificationScore: '',
  };

  it('passes validation for valid form data without certification', () => {
    const errors = validateEmployeeForm(validData);
    expect(Object.keys(errors).length).toBe(0);
  });

  it('validates account name format (cannot start with digit)', () => {
    const err = validateEmployeeField('employeeLoginId', '1admin', validData);
    expect(err).toBe(MESSAGES.ERRORS.ER019());
  });

  it('validates password length (8 - 50 characters)', () => {
    const shortErr = validateEmployeeField('employeeLoginPassword', '1234567', validData);
    expect(shortErr).toBe(MESSAGES.ERRORS.ER007(LABELS.FIELDS.PASSWORD, 8, 50));

    const validErr = validateEmployeeField('employeeLoginPassword', '12345678', validData);
    expect(validErr).toBe('');
  });

  it('validates confirm password match', () => {
    const dataMismatch = {
      ...validData,
      employeeLoginPassword: 'password123',
      employeeLoginPasswordConfirm: 'different123',
    };
    const errors = validateEmployeeForm(dataMismatch);
    expect(errors.employeeLoginPasswordConfirm).toBe(MESSAGES.ERRORS.ER017());
  });

  it('validates date fields return ER002 when empty', () => {
    const errBirthDate = validateEmployeeField('employeeBirthDate', '', validData);
    expect(errBirthDate).toBe(MESSAGES.ERRORS.ER002(LABELS.FIELDS.BIRTH_DATE));

    const dataWithEmptyCertDates: EmployeeFormData = {
      ...validData,
      certificationId: '1',
      certificationStartDate: '',
      certificationEndDate: '',
      employeeCertificationScore: '95',
    };
    const errors = validateEmployeeForm(dataWithEmptyCertDates);
    expect(errors.certificationStartDate).toBe(MESSAGES.ERRORS.ER002(LABELS.FIELDS.CERTIFICATION_START_DATE));
    expect(errors.certificationEndDate).toBe(MESSAGES.ERRORS.ER002(LABELS.FIELDS.EXPIRATION_DATE));
  });

  it('validates email field returns ER008 when containing non-halfsize characters', () => {
    // Full-width (Zenkaku) email
    const fullWidthErr = validateEmployeeField('employeeEmail', 'ｔｅｓｔ＠ｇｍａｉｌ．ｃｏｍ', validData);
    expect(fullWidthErr).toBe(MESSAGES.ERRORS.ER008(LABELS.FIELDS.EMAIL));

    // Valid halfsize email format
    const validErr = validateEmployeeField('employeeEmail', 'test@luvina.net', validData);
    expect(validErr).toBe('');
  });

  it('validates certification fields when certification is selected', () => {
    const dataWithCert: EmployeeFormData = {
      ...validData,
      certificationId: '1',
      certificationStartDate: '2023/01/01',
      certificationEndDate: '2022/01/01', // End date before start date
      employeeCertificationScore: '95',
    };
    const errors = validateEmployeeForm(dataWithCert);
    expect(errors.certificationEndDate).toBe(MESSAGES.ERRORS.ER012());
  });

  it('validates kana name returns ER009 for full-width Katakana and accepts half-width', () => {
    const fullWidthErr = validateEmployeeField('employeeNameKana', 'グエン ヴァン エー', validData);
    expect(fullWidthErr).toBe(MESSAGES.ERRORS.ER009(LABELS.FIELDS.FULL_NAME_KANA));

    const halfWidthErr = validateEmployeeField('employeeNameKana', 'ｸﾞｴﾝ ｳﾞｧﾝ ｴｰ', validData);
    expect(halfWidthErr).toBe('');
  });
});

