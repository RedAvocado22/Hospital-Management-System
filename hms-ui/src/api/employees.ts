import api from './axiosInstance';
import type { ApiResponse, PaginatedResponse, Employee, CreateEmployeeRequest, UpdateEmployeeRequest } from '../types';

export const getEmployees = (params: {
  page?: number;
  size?: number;
  name?: string;
  department?: string;
  roleName?: string;
  isActive?: boolean;
  hireDateFrom?: string;
  hireDateTo?: string;
}) => api.get<ApiResponse<PaginatedResponse<Employee>>>('/employees', { params });

export const getEmployeeById = (id: string) =>
  api.get<ApiResponse<Employee>>(`/employees/${id}`);

export const createEmployee = (data: CreateEmployeeRequest) =>
  api.post<ApiResponse<Employee>>('/employees', data);

export const updateEmployee = (id: string, data: UpdateEmployeeRequest) =>
  api.put<ApiResponse<Employee>>(`/employees/${id}`, data);

export const deactivateEmployee = (id: string) =>
  api.patch<ApiResponse<unknown>>(`/employees/${id}/deactivate`);

export const activateEmployee = (id: string) =>
  api.patch<ApiResponse<unknown>>(`/employees/${id}/activate`);
