import { Form, Input, Button, Card, Typography, Alert, Select, DatePicker, Row, Col } from 'antd';
import { MedicineBoxOutlined } from '@ant-design/icons';
import { useNavigate, Link } from 'react-router-dom';
import { useState } from 'react';
import { signUp } from '../../api/auth';
import dayjs from 'dayjs';

const { Title, Text } = Typography;

export default function RegisterPage() {
  const navigate = useNavigate();
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const onFinish = async (values: {
    username: string;
    email: string;
    firstName: string;
    lastName: string;
    password: string;
    dob: dayjs.Dayjs;
    gender: string;
    address: string;
    phone: string;
  }) => {
    setError('');
    setLoading(true);
    try {
      await signUp({
        ...values,
        dob: values.dob.format('YYYY-MM-DD'),
      });
      navigate('/login');
    } catch (err: unknown) {
      const axiosErr = err as { response?: { data?: { message?: string } } };
      setError(axiosErr.response?.data?.message ?? 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        background: '#F5F7FA',
        padding: '24px',
      }}
    >
      <Card
        style={{ width: 560, borderRadius: 12, boxShadow: '0 4px 24px rgba(0,0,0,0.08)' }}
        styles={{ body: { padding: '40px' } }}
      >
        <div style={{ textAlign: 'center', marginBottom: 32 }}>
          <MedicineBoxOutlined style={{ fontSize: 48, color: '#0D9488' }} />
          <Title level={2} style={{ marginTop: 12, marginBottom: 4 }}>
            Patient Registration
          </Title>
          <Text type="secondary">Create your HMS account</Text>
        </div>

        {error && (
          <Alert message={error} type="error" showIcon style={{ marginBottom: 16 }} />
        )}

        <Form layout="vertical" onFinish={onFinish} size="large">
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="First Name" name="firstName" rules={[{ required: true }]}>
                <Input placeholder="First name" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Last Name" name="lastName" rules={[{ required: true }]}>
                <Input placeholder="Last name" />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item label="Username" name="username" rules={[{ required: true }]}>
            <Input placeholder="Choose a username" />
          </Form.Item>

          <Form.Item
            label="Email"
            name="email"
            rules={[{ required: true }, { type: 'email', message: 'Enter a valid email' }]}
          >
            <Input placeholder="Email address" />
          </Form.Item>

          <Form.Item
            label="Password"
            name="password"
            rules={[{ required: true }, { min: 6, message: 'Minimum 6 characters' }]}
          >
            <Input.Password placeholder="Password" />
          </Form.Item>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="Date of Birth" name="dob" rules={[{ required: true }]}>
                <DatePicker style={{ width: '100%' }} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Gender" name="gender" rules={[{ required: true }]}>
                <Select placeholder="Select gender">
                  <Select.Option value="MALE">Male</Select.Option>
                  <Select.Option value="FEMALE">Female</Select.Option>
                </Select>
              </Form.Item>
            </Col>
          </Row>

          <Form.Item label="Phone" name="phone" rules={[{ required: true }]}>
            <Input placeholder="Phone number" />
          </Form.Item>

          <Form.Item label="Address" name="address" rules={[{ required: true }]}>
            <Input.TextArea placeholder="Home address" rows={2} />
          </Form.Item>

          <Form.Item style={{ marginBottom: 8 }}>
            <Button type="primary" htmlType="submit" block loading={loading}>
              Register
            </Button>
          </Form.Item>
        </Form>

        <div style={{ textAlign: 'center', marginTop: 16 }}>
          <Text type="secondary">Already have an account? </Text>
          <Link to="/login">Sign in</Link>
        </div>
      </Card>
    </div>
  );
}
