import { DashboardOutlined } from '@ant-design/icons';
import AppLayout from '../../components/AppLayout';

const navItems = [
  { key: 'dashboard', icon: <DashboardOutlined />, label: 'Dashboard', path: '/patient/dashboard' },
];

export default function PatientLayout() {
  return <AppLayout navItems={navItems} role="Patient" />;
}
