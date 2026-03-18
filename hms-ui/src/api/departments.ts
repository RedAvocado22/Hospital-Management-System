import api from './axiosInstance';
import type { ApiResponse, Department } from '../types';

export const getDepartments = (params?: { page?: number; size?: number }) =>
  api.get<ApiResponse<Department[]>>('/departments', { params });
