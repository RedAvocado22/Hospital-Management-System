import { Card, Row, Col, Typography, Avatar, Descriptions, Tag, Spin } from 'antd';
import {
  UserOutlined,
  CalendarOutlined,
  FileTextOutlined,
  BellOutlined,
} from '@ant-design/icons';
import { useAuthStore } from '../../store/authStore';
import { useQuery } from '@tanstack/react-query';
import { getAppointments } from '../../api/appointments';
import { getMedicalRecords } from '../../api/medicalRecords';

const { Title, Text } = Typography;

export default function PatientDashboard() {
  const { user } = useAuthStore();

  const { data: appointmentsData, isLoading: appointmentsLoading } = useQuery({
    queryKey: ['appointments-pending-count'],
    queryFn: () => getAppointments({ page: 0, size: 1, status: 'PENDING' }).then((r) => r.data),
  });

  const { data: recordsData, isLoading: recordsLoading } = useQuery({
    queryKey: ['medical-records-count'],
    queryFn: () => getMedicalRecords({ page: 0, size: 1 }).then((r) => r.data),
  });

  const pendingAppointments = appointmentsData?.data?.totalElements ?? 0;
  const totalRecords = recordsData?.data?.totalElements ?? 0;

  const stats = [
    {
      title: 'Upcoming Appointments',
      value: pendingAppointments,
      loading: appointmentsLoading,
      icon: <CalendarOutlined />,
      color: '#0D9488',
    },
    {
      title: 'Medical Records',
      value: totalRecords,
      loading: recordsLoading,
      icon: <FileTextOutlined />,
      color: '#16A34A',
    },
    {
      title: 'Notifications',
      value: 0,
      loading: false,
      icon: <BellOutlined />,
      color: '#F59E0B',
    },
  ];

  return (
    <div>
      <Title level={3} style={{ marginBottom: 24 }}>
        Welcome back, {user?.username}
      </Title>

      {/* Profile Card */}
      <Card style={{ marginBottom: 24, borderRadius: 8 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 24 }}>
          <Avatar size={80} icon={<UserOutlined />} style={{ backgroundColor: '#0D9488', flexShrink: 0 }} />
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
            <Card style={{ borderRadius: 8 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
                <div style={{
                  width: 44, height: 44, borderRadius: 10,
                  background: `${stat.color}18`,
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  fontSize: 20, color: stat.color, flexShrink: 0,
                }}>
                  {stat.icon}
                </div>
                <div>
                  {stat.loading ? (
                    <Spin size="small" />
                  ) : (
                    <Title level={3} style={{ margin: 0 }}>{stat.value}</Title>
                  )}
                  <Text type="secondary" style={{ fontSize: 13 }}>{stat.title}</Text>
                </div>
              </div>
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
