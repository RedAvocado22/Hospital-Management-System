import api from "./axiosInstance";
import type { ApiResponse, PaginatedResponse, DoctorSchedule } from "../types";

export const getDoctorSchedules = (params: {
    date?: string;
    doctorId?: string;
    page?: number;
    size?: number;
}) =>
    api.get<ApiResponse<PaginatedResponse<DoctorSchedule>>>("/doctor-schedules", {
        params,
    });

export const createDoctorSchedule = (data: {
    doctorId: string;
    date: string;
    type: string;
    maxPatients: number;
}) => api.post<ApiResponse<DoctorSchedule>>("/doctor-schedules", data);

export const deactivateDoctorSchedule = (id: string) =>
    api.patch<ApiResponse<unknown>>(`/doctor-schedules/${id}/deactivate`);
