import api from './axiosInstance';
import type { ApiResponse, PaginatedResponse, Employee, CreateEmployeeRequest } from '../types';

export const getEmployees = (params: {
  page?: number;
  size?: number;
  keyword?: string;
  departmentId?: string;
  role?: string;
  isActive?: boolean;
}) => api.get<ApiResponse<PaginatedResponse<Employee>>>('/employees', { params });

export const getEmployeeById = (id: string) =>
  api.get<ApiResponse<Employee>>(`/employees/${id}`);

export const createEmployee = (data: CreateEmployeeRequest) =>
  api.post<ApiResponse<Employee>>('/employees', data);

export const deactivateEmployee = (id: string) =>
  api.patch<ApiResponse<unknown>>(`/employees/${id}/deactivate`);

export const activateEmployee = (id: string) =>
  api.patch<ApiResponse<unknown>>(`/employees/${id}/activate`);
