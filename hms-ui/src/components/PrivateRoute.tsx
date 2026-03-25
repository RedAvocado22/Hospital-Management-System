import { Navigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';
import { isTokenExpired } from '../utils/jwt';

interface Props {
  children: React.ReactNode;
  allowedRoles: string[];
}

export default function PrivateRoute({ children, allowedRoles }: Props) {
  const { isAuthenticated, user, accessToken, logout } = useAuthStore();

  if (!isAuthenticated || !accessToken) {
    return <Navigate to="/login" replace />;
  }

  if (isTokenExpired(accessToken)) {
    logout();
    return <Navigate to="/login" replace />;
  }

  const hasRole = user?.roles.some((r) => allowedRoles.includes(r));
  if (!hasRole) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <>{children}</>;
}
