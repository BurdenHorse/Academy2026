/**
 * Định nghĩa vai trò của người dùng trong hệ thống:
 * 0: User thông thường
 * 1: Admin quản trị viên
 */
export const ROLES = {
  USER: 0,
  ADMIN: 1,
} as const;

export type RoleType = typeof ROLES[keyof typeof ROLES];
