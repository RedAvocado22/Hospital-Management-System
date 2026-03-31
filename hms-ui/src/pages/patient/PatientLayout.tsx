import { DashboardOutlined, CalendarOutlined, FileTextOutlined, UserOutlined } from '@ant-design/icons';
import AppLayout from '../../components/AppLayout';

const navItems = [
  { key: 'dashboard', icon: <DashboardOutlined />, label: 'Dashboard', path: '/patient/dashboard' },
  { key: 'appointments', icon: <CalendarOutlined />, label: 'My Appointments', path: '/patient/appointments' },
  { key: 'medical-records', icon: <FileTextOutlined />, label: 'My Medical Records', path: '/patient/medical-records' },
  { key: 'profile', icon: <UserOutlined />, label: 'My Profile', path: '/patient/profile' },
];

export default function PatientLayout() {
  return <AppLayout navItems={navItems} role="Patient" />;
}
