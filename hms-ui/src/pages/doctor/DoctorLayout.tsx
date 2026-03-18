import { FileTextOutlined } from '@ant-design/icons';
import AppLayout from '../../components/AppLayout';

const navItems = [
  { key: 'medical-records', icon: <FileTextOutlined />, label: 'My Medical Records', path: '/doctor/medical-records' },
];

export default function DoctorLayout() {
  return <AppLayout navItems={navItems} role="Doctor" />;
}
