import type { JwtPayload, UserInfo } from '../types';
import { ROLES } from '../constants/roles';

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
  const priority = [ROLES.ADMIN, ROLES.DOCTOR, ROLES.RECEPTIONIST, ROLES.PHARMACIST, ROLES.CASHIER, ROLES.PATIENT];
  return priority.find((r) => roles.includes(r)) ?? ROLES.PATIENT;
}

export function isTokenExpired(token: string): boolean {
  const payload = decodeJwt(token);
  if (!payload) return true;
  return payload.exp * 1000 < Date.now();
}

export function getRoleRedirectPath(roles: string[]): string {
  const role = getPrimaryRole(roles);
  switch (role) {
    case ROLES.ADMIN: return '/admin/employees';
    case ROLES.DOCTOR: return '/doctor/medical-records';
    case ROLES.RECEPTIONIST: return '/receptionist/medical-records';
    default: return '/patient/dashboard';
  }
}
