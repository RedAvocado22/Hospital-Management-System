import api from './axiosInstance';
import type { ApiResponse, Department, PaginatedResponse } from '../types';

export const getDepartments = (params?: { page?: number; size?: number }) =>
  api.get<ApiResponse<PaginatedResponse<Department>>>('/departments', { params });
