import { MedicineBoxOutlined } from '@ant-design/icons';
import AppLayout from '../../components/AppLayout';

const navItems = [
  { key: 'medicines', icon: <MedicineBoxOutlined />, label: 'Medicines', path: '/pharmacist/medicines' },
];

export default function PharmacistLayout() {
  return <AppLayout navItems={navItems} role="Pharmacist" />;
}
