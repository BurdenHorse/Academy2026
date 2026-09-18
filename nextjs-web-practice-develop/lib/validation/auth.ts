import { z } from 'zod';
import { MESSAGES } from '@/constants';

export const loginSchema = z.object({
  username: z.string().min(1, MESSAGES.AUTH.USERNAME_REQUIRED),
  password: z.string().min(1, MESSAGES.AUTH.PASSWORD_REQUIRED),
});

export type LoginForm = z.infer<typeof loginSchema>;

