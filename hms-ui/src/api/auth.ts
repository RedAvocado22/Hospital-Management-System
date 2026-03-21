import api from './axiosInstance';
import type { ApiResponse, AuthResponse } from '../types';

export const signIn = (username: string, password: string) =>
  api.post<ApiResponse<AuthResponse>>('/auth/signin', { username, password });

export const signUp = (data: {
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  password: string;
  dob: string;
  gender: string;
  address: string;
  phone: string;
}) => api.post<ApiResponse<unknown>>('/auth/signup', data);
