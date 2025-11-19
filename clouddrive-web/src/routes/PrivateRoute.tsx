// src/routes/PrivateRoute.tsx
import { Navigate, Outlet } from 'react-router-dom';

function useAuth() {
  // replace with your real auth mechanism
  const token = localStorage.getItem('token');
  return Boolean(token);
}

export default function PrivateRoute() {
  const authed = useAuth();
  return authed ? <Outlet /> : <Navigate to="/login" replace />;
}
