import { Layout, Menu, Avatar, Dropdown, Typography, Space } from 'antd';
import {
  TeamOutlined,
  FileTextOutlined,
  LogoutOutlined,
  UserOutlined,
  MedicineBoxOutlined,
} from '@ant-design/icons';
import { useNavigate, useLocation, Outlet } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';

const { Sider, Header, Content } = Layout;
const { Text } = Typography;

interface NavItem {
  key: string;
  icon: React.ReactNode;
  label: string;
  path: string;
}

interface Props {
  navItems: NavItem[];
  role: string;
}

export default function AppLayout({ navItems, role }: Props) {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuthStore();

  const selectedKey = navItems.find((item) => location.pathname.startsWith(item.path))?.key ?? '';

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const userMenuItems = [
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: 'Logout',
      onClick: handleLogout,
    },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider
        width={240}
        style={{
          background: '#001529',
          position: 'fixed',
          height: '100vh',
          left: 0,
          top: 0,
          zIndex: 100,
        }}
      >
        <div style={{ padding: '24px 16px', borderBottom: '1px solid rgba(255,255,255,0.1)' }}>
          <Space>
            <MedicineBoxOutlined style={{ fontSize: 24, color: '#1890ff' }} />
            <div>
              <Text strong style={{ color: '#fff', fontSize: 16, display: 'block' }}>
                HMS
              </Text>
              <Text style={{ color: 'rgba(255,255,255,0.45)', fontSize: 12 }}>{role}</Text>
            </div>
          </Space>
        </div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[selectedKey]}
          style={{ marginTop: 8 }}
          items={navItems.map((item) => ({
            key: item.key,
            icon: item.icon,
            label: item.label,
            onClick: () => navigate(item.path),
          }))}
        />
      </Sider>

      <Layout style={{ marginLeft: 240 }}>
        <Header
          style={{
            background: '#fff',
            padding: '0 24px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'flex-end',
            boxShadow: '0 1px 4px rgba(0,21,41,0.08)',
            position: 'sticky',
            top: 0,
            zIndex: 99,
          }}
        >
          <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
            <Space style={{ cursor: 'pointer' }}>
              <Avatar icon={<UserOutlined />} style={{ backgroundColor: '#1890ff' }} />
              <Text strong>{user?.username}</Text>
            </Space>
          </Dropdown>
        </Header>

        <Content style={{ margin: '24px', minHeight: 'calc(100vh - 64px - 48px)' }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
}

export { TeamOutlined, FileTextOutlined };
