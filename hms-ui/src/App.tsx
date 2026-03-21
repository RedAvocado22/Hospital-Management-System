import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ConfigProvider } from 'antd';

import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';
import PrivateRoute from './components/PrivateRoute';

import AdminLayout from './pages/admin/AdminLayout';
import EmployeeListPage from './pages/admin/EmployeeListPage';
import CreateEmployeePage from './pages/admin/CreateEmployeePage';

import DoctorLayout from './pages/doctor/DoctorLayout';
import ReceptionistLayout from './pages/receptionist/ReceptionistLayout';
import PatientLayout from './pages/patient/PatientLayout';
import PatientDashboard from './pages/patient/PatientDashboard';
import MedicalRecordsPage from './pages/shared/MedicalRecordsPage';

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
            colorPrimary: '#1890ff',
            borderRadius: 6,
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
                <PrivateRoute allowedRoles={['ROLE_ADMIN']}>
                  <AdminLayout />
                </PrivateRoute>
              }
            >
              <Route index element={<Navigate to="employees" replace />} />
              <Route path="employees" element={<EmployeeListPage />} />
              <Route path="employees/create" element={<CreateEmployeePage />} />
              <Route path="medical-records" element={<MedicalRecordsPage />} />
            </Route>

            {/* Doctor */}
            <Route
              path="/doctor"
              element={
                <PrivateRoute allowedRoles={['ROLE_DOCTOR']}>
                  <DoctorLayout />
                </PrivateRoute>
              }
            >
              <Route index element={<Navigate to="medical-records" replace />} />
              <Route path="medical-records" element={<MedicalRecordsPage />} />
            </Route>

            {/* Receptionist */}
            <Route
              path="/receptionist"
              element={
                <PrivateRoute allowedRoles={['ROLE_RECEPTIONIST']}>
                  <ReceptionistLayout />
                </PrivateRoute>
              }
            >
              <Route index element={<Navigate to="medical-records" replace />} />
              <Route path="medical-records" element={<MedicalRecordsPage />} />
            </Route>

            {/* Patient */}
            <Route
              path="/patient"
              element={
                <PrivateRoute allowedRoles={['ROLE_PATIENT']}>
                  <PatientLayout />
                </PrivateRoute>
              }
            >
              <Route index element={<Navigate to="dashboard" replace />} />
              <Route path="dashboard" element={<PatientDashboard />} />
            </Route>

            {/* Fallback */}
            <Route path="/" element={<Navigate to="/login" replace />} />
            <Route path="/unauthorized" element={<div style={{ padding: 40, textAlign: 'center' }}>403 — Access Denied</div>} />
            <Route path="*" element={<Navigate to="/login" replace />} />
          </Routes>
        </BrowserRouter>
      </ConfigProvider>
    </QueryClientProvider>
  );
}
