import api from "./axiosInstance";
import type { ApiResponse, PaginatedResponse, Appointment } from "../types";

export const getAppointments = (params: {
    page?: number;
    size?: number;
    status?: string;
    date?: string;
}) =>
    api.get<ApiResponse<PaginatedResponse<Appointment>>>("/appointments", {
        params,
    });

export const bookAppointment = (data: {
    scheduleId: string;
    reason: string;
    patientId?: string;
}) => api.post<ApiResponse<Appointment>>("/appointments", data);

export const cancelAppointment = (id: string) =>
    api.patch<ApiResponse<unknown>>(`/appointments/${id}/cancel`);

export const confirmAppointment = (id: string) =>
    api.patch<ApiResponse<unknown>>(`/appointments/${id}/confirm`);
