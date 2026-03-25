import { Form, Input, Button, Card, Typography, Select, DatePicker, Row, Col, Alert, message, Space } from 'antd';
import { ArrowLeftOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { useQuery, useMutation } from '@tanstack/react-query';
import { useState } from 'react';
import { createEmployee } from '../../api/employees';
import { getDepartments } from '../../api/departments';
import type { CreateEmployeeRequest } from '../../types';
import dayjs from 'dayjs';

const { Title } = Typography;

export default function CreateEmployeePage() {
  const navigate = useNavigate();
  const [error, setError] = useState('');

  const { data: deptData } = useQuery({
    queryKey: ['departments'],
    queryFn: () => getDepartments({ size: 100 }).then((r) => r.data),
  });

  const mutation = useMutation({
    mutationFn: createEmployee,
    onSuccess: () => {
      message.success('Employee created successfully');
      navigate('/admin/employees');
    },
    onError: (err: unknown) => {
      const axiosErr = err as { response?: { data?: { message?: string } } };
      setError(axiosErr.response?.data?.message ?? 'Failed to create employee');
    },
  });

  const onFinish = (values: CreateEmployeeRequest & { dob: dayjs.Dayjs; hireDate: dayjs.Dayjs }) => {
    setError('');
    mutation.mutate({
      ...values,
      dob: values.dob.format('YYYY-MM-DD'),
      hireDate: values.hireDate.format('YYYY-MM-DD'),
    });
  };

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 24 }}>
        <Button icon={<ArrowLeftOutlined />} onClick={() => navigate('/admin/employees')} />
        <Title level={3} style={{ margin: 0 }}>Add New Employee</Title>
      </div>

      <Card style={{ borderRadius: 8, maxWidth: 800 }}>
        {error && <Alert message={error} type="error" showIcon style={{ marginBottom: 16 }} />}

        <Form layout="vertical" onFinish={onFinish} size="large">
          <Title level={5} style={{ marginBottom: 16, paddingBottom: 12, borderBottom: '1px solid #EEF1F5' }}>Personal Information</Title>
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="First Name" name="firstName" rules={[{ required: true }, { min: 2, message: 'At least 2 characters' }, { max: 50, message: 'Max 50 characters' }]}>
                <Input placeholder="First name" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Last Name" name="lastName" rules={[{ required: true }, { min: 2, message: 'At least 2 characters' }, { max: 50, message: 'Max 50 characters' }]}>
                <Input placeholder="Last name" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="Date of Birth" name="dob" rules={[{ required: true }]}>
                <DatePicker style={{ width: '100%' }} disabledDate={(d) => d.isAfter(new Date())} />
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

          <Form.Item
            label="Phone"
            name="phone"
            rules={[
              { required: true },
              { pattern: /^[0-9+\-\s]{9,15}$/, message: 'Enter a valid phone number' },
            ]}
          >
            <Input placeholder="Phone number" />
          </Form.Item>

          <Form.Item label="Address" name="address" rules={[{ required: true }, { min: 5, message: 'At least 5 characters' }]}>
            <Input.TextArea placeholder="Home address" rows={2} />
          </Form.Item>

          <Title level={5} style={{ marginTop: 8, marginBottom: 16, paddingBottom: 12, borderBottom: '1px solid #EEF1F5' }}>Account Information</Title>
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                label="Username"
                name="username"
                rules={[
                  { required: true },
                  { min: 3, message: 'At least 3 characters' },
                  { max: 30, message: 'Max 30 characters' },
                  { pattern: /^[a-zA-Z0-9_]+$/, message: 'Only letters, numbers and underscores' },
                ]}
              >
                <Input placeholder="Username" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Email" name="email" rules={[{ required: true }, { type: 'email', message: 'Enter a valid email' }]}>
                <Input placeholder="Email" />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item
            label="Password"
            name="password"
            rules={[{ required: true }, { min: 6, message: 'Minimum 6 characters' }]}
          >
            <Input.Password placeholder="Password" />
          </Form.Item>

          <Title level={5} style={{ marginTop: 8, marginBottom: 16, paddingBottom: 12, borderBottom: '1px solid #EEF1F5' }}>Employment Information</Title>
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                label="Employee Code"
                name="code"
                rules={[
                  { required: true },
                  { min: 2, message: 'At least 2 characters' },
                  { max: 20, message: 'Max 20 characters' },
                  { pattern: /^[A-Z0-9]+$/, message: 'Uppercase letters and numbers only' },
                ]}
              >
                <Input placeholder="e.g. EMP001" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Hire Date" name="hireDate" rules={[{ required: true }]}>
                <DatePicker style={{ width: '100%' }} />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="Role" name="role" rules={[{ required: true }]}>
                <Select placeholder="Select role">
                  <Select.Option value="DOCTOR">Doctor</Select.Option>
                  <Select.Option value="RECEPTIONIST">Receptionist</Select.Option>
                  <Select.Option value="PHARMACIST">Pharmacist</Select.Option>
                  <Select.Option value="CASHIER">Cashier</Select.Option>
                </Select>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Department" name="departmentId" rules={[{ required: true }]}>
                <Select placeholder="Select department" loading={!deptData}>
                  {deptData?.data?.map((dept) => (
                    <Select.Option key={dept.id} value={dept.id}>
                      {dept.name}
                    </Select.Option>
                  ))}
                </Select>
              </Form.Item>
            </Col>
          </Row>

          <Form.Item style={{ marginTop: 8 }}>
            <Space>
              <Button type="primary" htmlType="submit" loading={mutation.isPending}>
                Create Employee
              </Button>
              <Button onClick={() => navigate('/admin/employees')}>Cancel</Button>
            </Space>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
}

