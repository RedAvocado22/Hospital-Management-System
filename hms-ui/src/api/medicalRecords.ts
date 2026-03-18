import api from './axiosInstance';
import type { ApiResponse, PaginatedResponse, MedicalRecord } from '../types';

export const getMedicalRecords = (params: {
  page?: number;
  size?: number;
  keyword?: string;
  doctorName?: string;
  from?: string;
  to?: string;
}) => api.get<ApiResponse<PaginatedResponse<MedicalRecord>>>('/medical-records', { params });
