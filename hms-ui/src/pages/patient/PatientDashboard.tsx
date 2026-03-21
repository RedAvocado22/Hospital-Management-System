import { Card, Row, Col, Typography, Avatar, Descriptions, Tag } from 'antd';
import {
  UserOutlined,
  CalendarOutlined,
  FileTextOutlined,
  BellOutlined,
} from '@ant-design/icons';
import { useAuthStore } from '../../store/authStore';

const { Title, Text } = Typography;

export default function PatientDashboard() {
  const { user } = useAuthStore();

  const stats = [
    { title: 'Upcoming Appointments', value: 0, icon: <CalendarOutlined />, color: '#1890ff' },
    { title: 'Medical Records', value: 0, icon: <FileTextOutlined />, color: '#52c41a' },
    { title: 'Notifications', value: 0, icon: <BellOutlined />, color: '#faad14' },
  ];

  return (
    <div>
      <Title level={3} style={{ marginBottom: 24 }}>
        Welcome back, {user?.username}
      </Title>

      {/* Profile Card */}
      <Card style={{ marginBottom: 24, borderRadius: 8 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 24 }}>
          <Avatar size={80} icon={<UserOutlined />} style={{ backgroundColor: '#1890ff', flexShrink: 0 }} />
          <div style={{ flex: 1 }}>
            <Title level={4} style={{ marginBottom: 4 }}>
              {user?.username}
            </Title>
            <Text type="secondary">{user?.email}</Text>
            <div style={{ marginTop: 8 }}>
              <Tag color="blue">Patient</Tag>
            </div>
          </div>
        </div>
      </Card>

      {/* Stats */}
      <Row gutter={[16, 16]} style={{ marginBottom: 24 }}>
        {stats.map((stat) => (
          <Col xs={24} sm={8} key={stat.title}>
            <Card style={{ borderRadius: 8, textAlign: 'center' }}>
              <div style={{ fontSize: 32, color: stat.color, marginBottom: 8 }}>{stat.icon}</div>
              <Title level={2} style={{ margin: 0, color: stat.color }}>
                {stat.value}
              </Title>
              <Text type="secondary">{stat.title}</Text>
            </Card>
          </Col>
        ))}
      </Row>

      {/* Info Card */}
      <Card title="Account Information" style={{ borderRadius: 8 }}>
        <Descriptions column={1}>
          <Descriptions.Item label="Username">{user?.username}</Descriptions.Item>
          <Descriptions.Item label="Email">{user?.email}</Descriptions.Item>
          <Descriptions.Item label="Patient ID">
            <Text copyable>{user?.id}</Text>
          </Descriptions.Item>
          <Descriptions.Item label="Status">
            <Tag color="green">Active</Tag>
          </Descriptions.Item>
        </Descriptions>
      </Card>
    </div>
  );
}
