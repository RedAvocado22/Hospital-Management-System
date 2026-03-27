import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ConfigProvider } from "antd";

import LoginPage from "./pages/auth/LoginPage";
import RegisterPage from "./pages/auth/RegisterPage";
import PrivateRoute from "./components/PrivateRoute";

import AdminLayout from "./pages/admin/AdminLayout";
import EmployeeListPage from "./pages/admin/EmployeeListPage";
import CreateEmployeePage from "./pages/admin/CreateEmployeePage";

import DoctorLayout from "./pages/doctor/DoctorLayout";
import ReceptionistLayout from "./pages/receptionist/ReceptionistLayout";
import PatientLayout from "./pages/patient/PatientLayout";
import PatientDashboard from "./pages/patient/PatientDashboard";
import MedicalRecordsPage from "./pages/shared/MedicalRecordsPage";
import MedicalRecordDetailPage from "./pages/shared/MedicalRecordDetailPage";
import PharmacistLayout from "./pages/pharmacist/PharmacistLayout";
import MedicineListPage from "./pages/pharmacist/MedicineListPage";

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            retry: 1,
            staleTime: 30_000,
        },
    },
});

export default function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <ConfigProvider
                theme={{
                    token: {
                        colorPrimary: "#0D9488",
                        borderRadius: 8,

                        // Typography
                        fontFamily:
                            "'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif",
                        fontSize: 14,
                        fontSizeLG: 15,
                        fontWeightStrong: 600,

                        // Backgrounds
                        colorBgLayout: "#F5F7FA",
                        colorBgContainer: "#FFFFFF",

                        // Borders
                        colorBorder: "#E5E9EF",
                        colorBorderSecondary: "#EEF1F5",

                        // Text
                        colorTextBase: "#1A2332",
                        colorTextSecondary: "#5C6B7A",

                        // Shadows
                        boxShadow:
                            "0 1px 3px rgba(0,0,0,0.08), 0 1px 2px rgba(0,0,0,0.04)",
                        boxShadowSecondary: "0 4px 12px rgba(0,0,0,0.08)",
                    },
                }}
            >
                <BrowserRouter>
                    <Routes>
                        {/* Public */}
                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />

                        {/* Admin */}
                        <Route
                            path="/admin"
                            element={
                                <PrivateRoute allowedRoles={["ROLE_ADMIN"]}>
                                    <AdminLayout />
                                </PrivateRoute>
                            }
                        >
                            <Route
                                index
                                element={<Navigate to="employees" replace />}
                            />
                            <Route
                                path="employees"
                                element={<EmployeeListPage />}
                            />
                            <Route
                                path="employees/create"
                                element={<CreateEmployeePage />}
                            />
                            <Route
                                path="medical-records"
                                element={<MedicalRecordsPage />}
                            />
                            <Route
                                path="medical-records/:id"
                                element={<MedicalRecordDetailPage />}
                            />
                        </Route>

                        {/* Doctor */}
                        <Route
                            path="/doctor"
                            element={
                                <PrivateRoute allowedRoles={["ROLE_DOCTOR"]}>
                                    <DoctorLayout />
                                </PrivateRoute>
                            }
                        >
                            <Route
                                index
                                element={
                                    <Navigate to="medical-records" replace />
                                }
                            />
                            <Route
                                path="medical-records"
                                element={<MedicalRecordsPage />}
                            />
                            <Route
                                path="medical-records/:id"
                                element={<MedicalRecordDetailPage />}
                            />
                        </Route>

                        {/* Receptionist */}
                        <Route
                            path="/receptionist"
                            element={
                                <PrivateRoute
                                    allowedRoles={["ROLE_RECEPTIONIST"]}
                                >
                                    <ReceptionistLayout />
                                </PrivateRoute>
                            }
                        >
                            <Route
                                index
                                element={
                                    <Navigate to="medical-records" replace />
                                }
                            />
                            <Route
                                path="medical-records"
                                element={<MedicalRecordsPage />}
                            />
                            <Route
                                path="medical-records/:id"
                                element={<MedicalRecordDetailPage />}
                            />
                        </Route>

                        {/* Pharmacist */}
                        <Route
                            path="/pharmacist"
                            element={
                                <PrivateRoute allowedRoles={["ROLE_PHARMACIST"]}>
                                    <PharmacistLayout />
                                </PrivateRoute>
                            }
                        >
                            <Route
                                index
                                element={<Navigate to="medicines" replace />}
                            />
                            <Route
                                path="medicines"
                                element={<MedicineListPage />}
                            />
                        </Route>

                        {/* Patient */}
                        <Route
                            path="/patient"
                            element={
                                <PrivateRoute allowedRoles={["ROLE_PATIENT"]}>
                                    <PatientLayout />
                                </PrivateRoute>
                            }
                        >
                            <Route
                                index
                                element={<Navigate to="dashboard" replace />}
                            />
                            <Route
                                path="dashboard"
                                element={<PatientDashboard />}
                            />
                        </Route>

                        {/* Fallback */}
                        <Route
                            path="/"
                            element={<Navigate to="/login" replace />}
                        />
                        <Route
                            path="/unauthorized"
                            element={
                                <div
                                    style={{ padding: 40, textAlign: "center" }}
                                >
                                    403 — Access Denied
                                </div>
                            }
                        />
                        <Route
                            path="*"
                            element={<Navigate to="/login" replace />}
                        />
                    </Routes>
                </BrowserRouter>
            </ConfigProvider>
        </QueryClientProvider>
    );
}
