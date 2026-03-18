import { FileTextOutlined } from '@ant-design/icons';
import AppLayout from '../../components/AppLayout';

const navItems = [
  { key: 'medical-records', icon: <FileTextOutlined />, label: 'Medical Records', path: '/receptionist/medical-records' },
];

export default function ReceptionistLayout() {
  return <AppLayout navItems={navItems} role="Receptionist" />;
}
