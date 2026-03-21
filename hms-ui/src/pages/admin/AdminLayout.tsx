import { TeamOutlined, FileTextOutlined } from '@ant-design/icons';
import AppLayout from '../../components/AppLayout';

const navItems = [
  { key: 'employees', icon: <TeamOutlined />, label: 'Employees', path: '/admin/employees' },
  { key: 'medical-records', icon: <FileTextOutlined />, label: 'Medical Records', path: '/admin/medical-records' },
];

export default function AdminLayout() {
  return <AppLayout navItems={navItems} role="Administrator" />;
}
