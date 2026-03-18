import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import type { UserInfo } from '../types';
import { extractUserInfo } from '../utils/jwt';

interface AuthState {
  accessToken: string | null;
  refreshToken: string | null;
  user: UserInfo | null;
  isAuthenticated: boolean;
  setTokens: (accessToken: string, refreshToken: string) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      accessToken: null,
      refreshToken: null,
      user: null,
      isAuthenticated: false,

      setTokens: (accessToken, refreshToken) => {
        const user = extractUserInfo(accessToken);
        set({ accessToken, refreshToken, user, isAuthenticated: true });
      },

      logout: () => {
        set({ accessToken: null, refreshToken: null, user: null, isAuthenticated: false });
      },
    }),
    {
      name: 'hms-auth',
    }
  )
);
