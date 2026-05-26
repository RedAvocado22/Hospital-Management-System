import { FileTextOutlined, CalendarOutlined, UserOutlined } from "@ant-design/icons";
import AppLayout from "../../components/AppLayout";

const navItems = [
    {
        key: "medical-records",
        icon: <FileTextOutlined />,
        label: "My Medical Records",
        path: "/doctor/medical-records",
    },
    {
        key: "appointments",
        icon: <CalendarOutlined />,
        label: "Appointments",
        path: "/doctor/appointments",
    },
    {
        key: "patients",
        icon: <UserOutlined />,
        label: "Patients",
        path: "/doctor/patients",
    },
];

export default function DoctorLayout() {
    return <AppLayout navItems={navItems} role="Doctor" />;
}
