import type { JwtPayload, UserInfo } from '../types';

export function decodeJwt(token: string): JwtPayload | null {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload) as JwtPayload;
  } catch {
    return null;
  }
}

export function extractUserInfo(token: string): UserInfo | null {
  const payload = decodeJwt(token);
  if (!payload) return null;
  return {
    id: payload.sub,
    username: payload.preferred_username,
    email: payload.email,
    roles: (payload.realm_access?.roles ?? []).map((r) => r.startsWith('ROLE_') ? r : `ROLE_${r}`),
  };
}

export function getPrimaryRole(roles: string[]): string {
  const priority = ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST', 'ROLE_PHARMACIST', 'ROLE_CASHIER', 'ROLE_PATIENT'];
  return priority.find((r) => roles.includes(r)) ?? 'ROLE_PATIENT';
}

export function getRoleRedirectPath(roles: string[]): string {
  const role = getPrimaryRole(roles);
  switch (role) {
    case 'ROLE_ADMIN': return '/admin/employees';
    case 'ROLE_DOCTOR': return '/doctor/medical-records';
    case 'ROLE_RECEPTIONIST': return '/receptionist/medical-records';
    default: return '/patient/dashboard';
  }
}
