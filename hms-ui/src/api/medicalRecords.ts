import api from './axiosInstance';
import type { ApiResponse, PaginatedResponse, MedicalRecord, MedicalRecordDetail } from '../types';

export const getMedicalRecords = (params: {
  page?: number;
  size?: number;
  keyword?: string;
  doctorName?: string;
  from?: string;
  to?: string;
}) => api.get<ApiResponse<PaginatedResponse<MedicalRecord>>>('/medical-records', { params });

export const getMedicalRecordById = (id: string) =>
  api.get<ApiResponse<MedicalRecordDetail>>(`/medical-records/${id}`);

export const updateMedicalRecord = (id: string, data: { description: string; advice: string }) =>
  api.put<ApiResponse<MedicalRecordDetail>>(`/medical-records/${id}`, data);

export const createMedicalRecord = (data: {
  patientId: string;
  description: string;
  advice?: string;
}) => api.post<ApiResponse<MedicalRecordDetail>>('/medical-records', data);
