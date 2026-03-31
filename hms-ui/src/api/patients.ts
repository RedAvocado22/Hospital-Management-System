import api from './axiosInstance';
import type { ApiResponse, PaginatedResponse, Patient, PatientDetail } from '../types';

export const getPatients = (params: {
  page?: number;
  size?: number;
  fullName?: string;
  email?: string;
  phone?: string;
}) => api.get<ApiResponse<PaginatedResponse<Patient>>>('/patients', { params });

export const getPatientById = (id: string) =>
  api.get<ApiResponse<PatientDetail>>(`/patients/${id}`);

export const updatePatient = (id: string, data: {
  email?: string;
  firstName?: string;
  lastName?: string;
  dob?: string;
  gender?: string;
  address?: string;
  phone?: string;
  password?: string;
}) => api.put<ApiResponse<PatientDetail>>(`/patients/${id}`, data);

export const updatePatientMedical = (id: string, data: {
  bloodType?: string;
  allergies?: string;
}) => api.put<ApiResponse<PatientDetail>>(`/patients/${id}/medical`, data);
