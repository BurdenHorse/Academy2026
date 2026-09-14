export * from './routes';
export * from './messages';
export * from './labels';
export * from './codes';
export * from './roles';
export const DEFAULT_PAGE_LIMIT = 10;

export const STORAGE_KEYS = {
  TOKEN: 'token',
  FORM_DATA: 'ADM004_FORM_DATA',
  SERVER_ERROR: 'ADM004_SERVER_ERROR',
  DETAIL_EMPLOYEE_ID: 'ADM003_EMPLOYEE_ID',
  COMPLETE_MESSAGE_CODE: 'ADM006_MESSAGE_CODE',
  SYSTEM_ERROR_MESSAGE: 'SYSTEM_ERROR_MESSAGE',
} as const;
