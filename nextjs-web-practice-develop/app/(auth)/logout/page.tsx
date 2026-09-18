'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { removeToken } from '@/lib/auth/token';
import { ROUTES, LABELS } from '@/constants';

export default function LogoutPage() {
  const router = useRouter();

  useEffect(() => {
    removeToken();
    router.push(ROUTES.LOGIN);
  }, [router]);

  return <div>{LABELS.STATUS.LOGGING_OUT}</div>;
}

