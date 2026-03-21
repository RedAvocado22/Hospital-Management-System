import { Navigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';

interface Props {
  children: React.ReactNode;
  allowedRoles: string[];
}

export default function PrivateRoute({ children, allowedRoles }: Props) {
  const { isAuthenticated, user } = useAuthStore();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  const hasRole = user?.roles.some((r) => allowedRoles.includes(r));
  if (!hasRole) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <>{children}</>;
}
