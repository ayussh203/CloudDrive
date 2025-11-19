// src/components/PrivateRoute.tsx
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../store/auth';
import { useEffect } from 'react';

export default function PrivateRoute() {
  const { token, user, fetchMe } = useAuth();

  useEffect(() => {
    if (token && !user) {
      fetchMe().catch(() => {/* ignore; will redirect */});
    }
  }, [token, user, fetchMe]);

  if (!token) return <Navigate to="/login" replace />;
  return <Outlet />;
}
