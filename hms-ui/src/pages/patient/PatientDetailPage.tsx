import { Card, Descriptions, Tag, Button, Modal, Form, Input, Select, DatePicker, message, Typography, Space, Spin } from 'antd';
import { ArrowLeftOutlined, EditOutlined } from '@ant-design/icons';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useParams, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import dayjs from 'dayjs';
import { getPatientById, updatePatient } from '../../api/patients';
import { useAuthStore } from '../../store/authStore';

const { Title } = Typography;

export default function PatientDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { user } = useAuthStore();
  const isPatient = user?.roles?.includes('ROLE_PATIENT') ?? false;

  const [editOpen, setEditOpen] = useState(false);
  const [form] = Form.useForm();

  const { data, isLoading } = useQuery({
    queryKey: ['patient', id],
    queryFn: () => getPatientById(id!).then((r) => r.data),
    enabled: !!id,
  });

  const patient = data?.data;

  const updateMutation = useMutation({
    mutationFn: (values: Record<string, unknown>) => {
      const payload: Record<string, string> = {};
      if (values.email) payload.email = values.email as string;
      if (values.firstName) payload.firstName = values.firstName as string;
      if (values.lastName) payload.lastName = values.lastName as string;
      if (values.dob) payload.dob = dayjs(values.dob as string).format('YYYY-MM-DD');
      if (values.gender) payload.gender = values.gender as string;
      if (values.address) payload.address = values.address as string;
      if (values.phone) payload.phone = values.phone as string;
      if (values.password) payload.password = values.password as string;
      return updatePatient(id!, payload);
    },
    onSuccess: () => {
      message.success('Profile updated');
      setEditOpen(false);
      queryClient.invalidateQueries({ queryKey: ['patient', id] });
    },
    onError: () => message.error('Failed to update profile'),
  });

  const openEdit = () => {
    if (!patient) return;
    form.setFieldsValue({
      email: patient.email,
      firstName: patient.fullName.split(' ')[0],
      lastName: patient.fullName.split(' ').slice(1).join(' '),
      dob: patient.dob ? dayjs(patient.dob) : undefined,
      gender: patient.gender,
      address: patient.address,
      phone: patient.phone,
    });
    setEditOpen(true);
  };

  if (isLoading) return <Spin size="large" style={{ display: 'block', marginTop: 80 }} />;
  if (!patient) return <div>Patient not found</div>;

  return (
    <div>
      <Space style={{ marginBottom: 24 }}>
        <Button icon={<ArrowLeftOutlined />} onClick={() => navigate(-1)}>Back</Button>
        <Title level={3} style={{ margin: 0 }}>Patient Detail</Title>
        {isPatient && (
          <Button type="primary" icon={<EditOutlined />} onClick={openEdit}>
            Edit Profile
          </Button>
        )}
      </Space>

      <Card title="Account Information" style={{ borderRadius: 8, marginBottom: 16 }}>
        <Descriptions column={2} bordered size="small">
          <Descriptions.Item label="Full Name"><strong>{patient.fullName}</strong></Descriptions.Item>
          <Descriptions.Item label="Username">{patient.username}</Descriptions.Item>
          <Descriptions.Item label="Email">{patient.email}</Descriptions.Item>
          <Descriptions.Item label="Phone">{patient.phone ?? '—'}</Descriptions.Item>
          <Descriptions.Item label="Date of Birth">{patient.dob ? dayjs(patient.dob).format('DD/MM/YYYY') : '—'}</Descriptions.Item>
          <Descriptions.Item label="Gender">{patient.gender ?? '—'}</Descriptions.Item>
          <Descriptions.Item label="Address" span={2}>{patient.address ?? '—'}</Descriptions.Item>
          <Descriptions.Item label="Status">
            <Tag color={patient.active ? 'success' : 'error'}>{patient.active ? 'Active' : 'Inactive'}</Tag>
          </Descriptions.Item>
        </Descriptions>
      </Card>

      <Card title="Medical Profile" style={{ borderRadius: 8 }}>
        <Descriptions column={2} bordered size="small">
          <Descriptions.Item label="Blood Type">{patient.bloodType ?? '—'}</Descriptions.Item>
          <Descriptions.Item label="Allergies">{patient.allergies ?? '—'}</Descriptions.Item>
        </Descriptions>
      </Card>

      <Modal
        title="Edit Profile"
        open={editOpen}
        onCancel={() => setEditOpen(false)}
        onOk={() => form.submit()}
        confirmLoading={updateMutation.isPending}
        width={600}
      >
        <Form form={form} layout="vertical" onFinish={(vals) => updateMutation.mutate(vals)}>
          <Form.Item name="firstName" label="First Name">
            <Input />
          </Form.Item>
          <Form.Item name="lastName" label="Last Name">
            <Input />
          </Form.Item>
          <Form.Item name="email" label="Email" rules={[{ type: 'email' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="phone" label="Phone">
            <Input />
          </Form.Item>
          <Form.Item name="dob" label="Date of Birth">
            <DatePicker style={{ width: '100%' }} format="DD/MM/YYYY" />
          </Form.Item>
          <Form.Item name="gender" label="Gender">
            <Select allowClear>
              <Select.Option value="MALE">Male</Select.Option>
              <Select.Option value="FEMALE">Female</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="address" label="Address">
            <Input />
          </Form.Item>
          <Form.Item name="password" label="New Password (leave blank to keep current)">
            <Input.Password />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
