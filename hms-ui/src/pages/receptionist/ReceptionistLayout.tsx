import { FileTextOutlined, CalendarOutlined, UserOutlined } from '@ant-design/icons';
import AppLayout from '../../components/AppLayout';

const navItems = [
  { key: 'medical-records', icon: <FileTextOutlined />, label: 'Medical Records', path: '/receptionist/medical-records' },
  { key: 'appointments', icon: <CalendarOutlined />, label: 'Appointments', path: '/receptionist/appointments' },
  { key: 'patients', icon: <UserOutlined />, label: 'Patients', path: '/receptionist/patients' },
];

export default function ReceptionistLayout() {
  return <AppLayout navItems={navItems} role="Receptionist" />;
}
