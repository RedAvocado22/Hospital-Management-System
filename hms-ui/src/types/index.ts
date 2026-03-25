export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
  refreshExpiresIn: number;
}

export interface JwtPayload {
  sub: string;
  preferred_username: string;
  email: string;
  realm_access: {
    roles: string[];
  };
  exp: number;
}

export interface UserInfo {
  id: string;
  username: string;
  email: string;
  roles: string[];
}

export interface ApiResponse<T> {
  status: string;
  message: string;
  code: number;
  data: T;
}

export interface PaginatedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

export interface Employee {
  id: string;
  code: string;
  hireDate: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  fullName: string;
  dob: string;
  gender: string;
  address: string;
  phone: string;
  isActive: boolean;
  department: {
    id: string;
    name: string;
  };
  role: {
    id: string;
    name: string;
  };
}

export interface Department {
  id: string;
  name: string;
  isActive: boolean;
}

export interface MedicalRecord {
  id: string;
  patientName: string;
  doctorName: string;
  description: string;
  advice: string;
  createdAt: string;
}

export interface PatientInfoResponse {
  fullName: string;
  gender: string | null;
  dob: string | null;
  address: string | null;
  phone: string | null;
  email: string;
  bloodType: string | null;
  allergies: string | null;
}

export interface DoctorSummary {
  fullName: string;
  departmentName: string;
  code: string;
}

export interface MedicalRecordDetail {
  id: string;
  patient: PatientInfoResponse;
  doctor: DoctorSummary;
  description: string;
  advice: string;
  createdAt: string;
}

export interface CreateEmployeeRequest {
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  password: string;
  dob: string;
  gender: string;
  address: string;
  phone: string;
  code: string;
  hireDate: string;
  departmentId: string;
  role: string;
}
