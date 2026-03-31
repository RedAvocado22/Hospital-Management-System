import { TeamOutlined, FileTextOutlined, CalendarOutlined, UserOutlined, BankOutlined } from '@ant-design/icons';
import AppLayout from '../../components/AppLayout';

const navItems = [
  { key: 'employees', icon: <TeamOutlined />, label: 'Employees', path: '/admin/employees' },
  { key: 'medical-records', icon: <FileTextOutlined />, label: 'Medical Records', path: '/admin/medical-records' },
  { key: 'appointments', icon: <CalendarOutlined />, label: 'Appointments', path: '/admin/appointments' },
  { key: 'patients', icon: <UserOutlined />, label: 'Patients', path: '/admin/patients' },
  { key: 'departments', icon: <BankOutlined />, label: 'Departments', path: '/admin/departments' },
];

export default function AdminLayout() {
  return <AppLayout navItems={navItems} role="Administrator" />;
}
