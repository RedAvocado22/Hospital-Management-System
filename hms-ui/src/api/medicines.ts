import api from './axiosInstance';
import type { ApiResponse, PaginatedResponse, Medicine } from '../types';

export const getMedicines = (params: {
  page?: number;
  size?: number;
  name?: string;
  minPrice?: number;
  maxPrice?: number;
}) => api.get<ApiResponse<PaginatedResponse<Medicine>>>('/v1/medicines', { params });

export const getMedicineById = (id: string) =>
  api.get<ApiResponse<Medicine>>(`/v1/medicines/detail/${id}`);

export const createMedicine = (data: {
  name: string;
  quantity: number;
  price: number;
  description: string;
  isActive: boolean;
}) => api.post<ApiResponse<Medicine>>('/v1/medicines/create', data);

export const updateMedicine = (data: {
  id: string;
  name: string;
  quantity?: number;
  price?: number;
  description?: string;
}) => api.patch<ApiResponse<Medicine>>('/v1/medicines/update', data);

export const deactivateMedicine = (id: string) =>
  api.patch<ApiResponse<void>>('/v1/medicines/deactivate', { id });
