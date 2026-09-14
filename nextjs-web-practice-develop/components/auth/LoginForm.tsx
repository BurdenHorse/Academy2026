'use client';

import { useRouter } from 'next/navigation';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { authApi } from '@/lib/api/auth';
import { storeToken } from '@/lib/auth/token';
import { loginSchema, LoginForm as LoginFormType } from '@/lib/validation/auth';
import { ROUTES, MESSAGES, LABELS } from '@/constants';

/**
 * Component hiển thị form đăng nhập người dùng (ADM001).
 * Sử dụng React Hook Form kết hợp Zod Resolver để validate dữ liệu đầu vào,
 * gọi Auth API để xác thực và lưu token vào sessionStorage.
 *
 * @returns JSX.Element Form đăng nhập hoàn chỉnh
 */
export default function LoginForm() {
  const router = useRouter();
  const { register, handleSubmit, formState: { errors }, setError } = useForm<LoginFormType>({
    resolver: zodResolver(loginSchema),
  });

  /**
   * Xử lý gửi dữ liệu đăng nhập lên server.
   * Lưu access token nếu thành công và chuyển hướng tới trang danh sách nhân viên (ADM002).
   *
   * @param data Dữ liệu form đăng nhập hợp lệ (username, password)
   * @returns Promise<void>
   */
  const onSubmit = async (data: LoginFormType): Promise<void> => {
    try {
      const response = await authApi.login(data);
      storeToken(response.accessToken, response.tokenType);
      router.push(ROUTES.EMPLOYEES.LIST);
    } catch (error) {
      console.error('Login failed:', error);
      setError('root', {
        message: MESSAGES.ERRORS.LOGIN_FAILED,
      });
    }
  };

  return (
    <form className="login100-form validate-form" onSubmit={handleSubmit(onSubmit)}>
      {errors.root && (
        <span className="login100-form-title err">
          {errors.root.message}
        </span>
      )}
      {!errors.root && (
        <span className="login100-form-title">
          <br /><br />
        </span>
      )}

      <div className="wrap-input100 validate-input">
        <input 
          className="input100" 
          type="text" 
          placeholder={LABELS.PLACEHOLDERS.ACCOUNT_NAME} 
          {...register('username')}
        />
        <span className="focus-input100"></span>
        <span className="symbol-input100">
          <i className="fa fa-envelope" aria-hidden="true"></i>
        </span>
      </div>
      {errors.username && <span className="login100-form-title err" style={{ fontSize: '12px', paddingBottom: '10px' }}>{errors.username.message}</span>}

      <div className="wrap-input100 validate-input">
        <input 
          className="input100" 
          type="password" 
          placeholder={LABELS.PLACEHOLDERS.PASSWORD} 
          {...register('password')}
        />
        <span className="focus-input100"></span>
        <span className="symbol-input100">
          <i className="fa fa-lock" aria-hidden="true"></i>
        </span>
      </div>
      {errors.password && <span className="login100-form-title err" style={{ fontSize: '12px', paddingBottom: '10px' }}>{errors.password.message}</span>}

      <div className="container-login100-form-btn">
        <button type="submit" className="login100-form-btn">
          {LABELS.BUTTONS.LOGIN}
        </button>
      </div>
    </form>
  );
}
