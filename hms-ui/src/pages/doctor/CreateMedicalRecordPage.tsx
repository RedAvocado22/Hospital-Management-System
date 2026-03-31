import { Card, Form, Input, Button, Typography, Space, Descriptions, message, Spin } from 'antd';
import { ArrowLeftOutlined } from '@ant-design/icons';
import { useQuery, useMutation } from '@tanstack/react-query';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { getPatientById } from '../../api/patients';
import { createMedicalRecord } from '../../api/medicalRecords';

const { Title, Text } = Typography;
const { TextArea } = Input;

export default function CreateMedicalRecordPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const patientId = searchParams.get('patientId') ?? '';
  const patientName = searchParams.get('patientName') ?? '';
  const [form] = Form.useForm();

  const { data: patientData, isLoading: patientLoading } = useQuery({
    queryKey: ['patient', patientId],
    queryFn: () => getPatientById(patientId).then((r) => r.data),
    enabled: !!patientId,
  });

  const patient = patientData?.data;

  const createMutation = useMutation({
    mutationFn: (values: { description: string; advice?: string }) =>
      createMedicalRecord({ patientId, description: values.description, advice: values.advice }),
    onSuccess: (res) => {
      message.success('Medical record created');
      const recordId = res.data.data.id;
      navigate(`/doctor/medical-records/${recordId}`);
    },
    onError: () => message.error('Failed to create medical record'),
  });

  if (!patientId) {
    return (
      <div style={{ padding: 40, textAlign: 'center' }}>
        No patient selected. Go back and use "Create Record" from an appointment.
      </div>
    );
  }

  return (
    <div style={{ maxWidth: 720, margin: '0 auto' }}>
      <Space style={{ marginBottom: 24 }}>
        <Button icon={<ArrowLeftOutlined />} onClick={() => navigate(-1)}>Back</Button>
        <Title level={3} style={{ margin: 0 }}>Create Medical Record</Title>
      </Space>

      <Card title="Patient Information" style={{ borderRadius: 8, marginBottom: 20 }}>
        {patientLoading ? (
          <Spin />
        ) : patient ? (
          <Descriptions column={2} size="small">
            <Descriptions.Item label="Name"><strong>{patient.fullName}</strong></Descriptions.Item>
            <Descriptions.Item label="Phone">{patient.phone ?? '—'}</Descriptions.Item>
            <Descriptions.Item label="Blood Type">{patient.bloodType ?? '—'}</Descriptions.Item>
            <Descriptions.Item label="Allergies">{patient.allergies ?? '—'}</Descriptions.Item>
          </Descriptions>
        ) : (
          <Text type="secondary">Patient: {patientName}</Text>
        )}
      </Card>

      <Card title="Record Details" style={{ borderRadius: 8 }}>
        <Form form={form} layout="vertical" onFinish={(vals) => createMutation.mutate(vals)}>
          <Form.Item
            name="description"
            label="Description"
            rules={[
              { required: true, message: 'Description is required' },
              { min: 10, message: 'At least 10 characters' },
            ]}
          >
            <TextArea rows={5} placeholder="Describe the patient's condition, symptoms, diagnosis..." maxLength={2000} showCount />
          </Form.Item>
          <Form.Item name="advice" label="Doctor Advice">
            <TextArea rows={4} placeholder="Treatment plan, medication notes, follow-up instructions..." maxLength={2000} showCount />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={createMutation.isPending}>
              Create Record
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
}
