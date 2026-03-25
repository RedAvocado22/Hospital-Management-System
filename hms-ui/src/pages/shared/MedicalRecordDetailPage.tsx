import { Card, Descriptions, Button, Modal, Form, Input, Typography, Space, message, Spin, Alert } from 'antd';
import { ArrowLeftOutlined, EditOutlined } from '@ant-design/icons';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useParams, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { getMedicalRecordById, updateMedicalRecord } from '../../api/medicalRecords';
import { useAuthStore } from '../../store/authStore';
import dayjs from 'dayjs';

const { Title, Text } = Typography;
const { TextArea } = Input;

export default function MedicalRecordDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const user = useAuthStore((s) => s.user);
  const isDoctor = user?.roles.includes('ROLE_DOCTOR') ?? false;

  const [editOpen, setEditOpen] = useState(false);
  const [form] = Form.useForm();

  const { data, isLoading, isError } = useQuery({
    queryKey: ['medical-record', id],
    queryFn: () => getMedicalRecordById(id!).then((r) => r.data),
    enabled: !!id,
  });

  const record = data?.data;

  const updateMutation = useMutation({
    mutationFn: (values: { description: string; advice: string }) =>
      updateMedicalRecord(id!, values),
    onSuccess: () => {
      message.success('Medical record updated');
      queryClient.invalidateQueries({ queryKey: ['medical-record', id] });
      queryClient.invalidateQueries({ queryKey: ['medical-records'] });
      setEditOpen(false);
    },
    onError: () => message.error('Failed to update medical record'),
  });

  const openEdit = () => {
    form.setFieldsValue({
      description: record?.description,
      advice: record?.advice,
    });
    setEditOpen(true);
  };

  if (isLoading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', padding: 80 }}>
        <Spin size="large" />
      </div>
    );
  }

  if (isError || !record) {
    return (
      <div style={{ padding: 24 }}>
        <Alert
          type="error"
          message="Record not found"
          description="This medical record does not exist or you don't have access to it."
          action={
            <Button onClick={() => navigate(-1)}>Go Back</Button>
          }
        />
      </div>
    );
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <Space>
          <Button icon={<ArrowLeftOutlined />} onClick={() => navigate(-1)}>Back</Button>
          <Title level={3} style={{ margin: 0 }}>Medical Record Detail</Title>
        </Space>
        {isDoctor && (
          <Button type="primary" icon={<EditOutlined />} onClick={openEdit}>
            Edit
          </Button>
        )}
      </div>

      <Space direction="vertical" size={16} style={{ width: '100%' }}>
        <Card title="Patient Information" style={{ borderRadius: 8 }}>
          <Descriptions column={2} bordered size="small">
            <Descriptions.Item label="Full Name">{record.patient.fullName}</Descriptions.Item>
            <Descriptions.Item label="Email">{record.patient.email}</Descriptions.Item>
            <Descriptions.Item label="Gender">{record.patient.gender ?? '—'}</Descriptions.Item>
            <Descriptions.Item label="Date of Birth">
              {record.patient.dob ? dayjs(record.patient.dob).format('DD/MM/YYYY') : '—'}
            </Descriptions.Item>
            <Descriptions.Item label="Phone">{record.patient.phone ?? '—'}</Descriptions.Item>
            <Descriptions.Item label="Address">{record.patient.address ?? '—'}</Descriptions.Item>
            <Descriptions.Item label="Blood Type">{record.patient.bloodType ?? '—'}</Descriptions.Item>
            <Descriptions.Item label="Allergies">{record.patient.allergies ?? '—'}</Descriptions.Item>
          </Descriptions>
        </Card>

        <Card title="Doctor Information" style={{ borderRadius: 8 }}>
          <Descriptions column={2} bordered size="small">
            <Descriptions.Item label="Full Name">{record.doctor.fullName}</Descriptions.Item>
            <Descriptions.Item label="Code">{record.doctor.code}</Descriptions.Item>
            <Descriptions.Item label="Department">{record.doctor.departmentName}</Descriptions.Item>
          </Descriptions>
        </Card>

        <Card
          title="Record"
          extra={<Text type="secondary">{dayjs(record.createdAt).format('DD/MM/YYYY HH:mm')}</Text>}
          style={{ borderRadius: 8 }}
        >
          <Space direction="vertical" size={16} style={{ width: '100%' }}>
            <div>
              <Text strong>Description</Text>
              <div style={{ marginTop: 8, whiteSpace: 'pre-wrap' }}>{record.description}</div>
            </div>
            <div>
              <Text strong>Doctor Advice</Text>
              <div style={{ marginTop: 8, whiteSpace: 'pre-wrap' }}>{record.advice}</div>
            </div>
          </Space>
        </Card>
      </Space>

      <Modal
        title="Edit Medical Record"
        open={editOpen}
        onCancel={() => setEditOpen(false)}
        onOk={() => form.submit()}
        okText="Save"
        confirmLoading={updateMutation.isPending}
        destroyOnClose
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={(values) => updateMutation.mutate({ description: values.description, advice: values.advice })}
        >
          <Form.Item
            name="description"
            label="Description"
            rules={[{ required: true, message: 'Description is required' }]}
          >
            <TextArea rows={4} />
          </Form.Item>
          <Form.Item
            name="advice"
            label="Doctor Advice"
            rules={[{ required: true, message: 'Advice is required' }]}
          >
            <TextArea rows={4} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
