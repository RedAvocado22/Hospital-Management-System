import api from './axiosInstance';
import type { ApiResponse, Department, PaginatedResponse } from '../types';

export const getDepartments = (params?: { page?: number; size?: number }) =>
  api.get<ApiResponse<PaginatedResponse<Department>>>('/departments', { params });

export const createDepartment = (data: { name: string }) =>
  api.post<ApiResponse<Department>>('/departments', data);
