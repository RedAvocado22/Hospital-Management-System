import { Table, Card, Select, Space, Tag, Button, Popconfirm, message, Typography, DatePicker } from 'antd';
import { CalendarOutlined } from '@ant-design/icons';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import dayjs from 'dayjs';
import { getAppointments, cancelAppointment, confirmAppointment } from '../../api/appointments';
import { useAuthStore } from '../../store/authStore';
import type { Appointment } from '../../types';

const { Title } = Typography;

const STATUS_COLORS: Record<string, string> = {
  PENDING: 'orange',
  CONFIRMED: 'blue',
  COMPLETED: 'success',
  CANCELLED: 'red',
};

export default function AppointmentListPage() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { user } = useAuthStore();
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [statusFilter, setStatusFilter] = useState<string | undefined>();
  const [dateFilter, setDateFilter] = useState<string | undefined>();

  const isAdmin = user?.roles?.includes('ROLE_ADMIN') ?? false;
  const isDoctor = user?.roles?.includes('ROLE_DOCTOR') ?? false;
  const isReceptionist = user?.roles?.includes('ROLE_RECEPTIONIST') ?? false;
  const isPatient = user?.roles?.includes('ROLE_PATIENT') ?? false;

  const { data, isLoading } = useQuery({
    queryKey: ['appointments', page, pageSize, statusFilter, dateFilter],
    queryFn: () =>
      getAppointments({ page: page - 1, size: pageSize, status: statusFilter, date: dateFilter }).then((r) => r.data),
  });

  const cancelMutation = useMutation({
    mutationFn: cancelAppointment,
    onSuccess: () => {
      message.success('Appointment cancelled');
      queryClient.invalidateQueries({ queryKey: ['appointments'] });
    },
    onError: () => message.error('Failed to cancel appointment'),
  });

  const confirmMutation = useMutation({
    mutationFn: confirmAppointment,
    onSuccess: () => {
      message.success('Appointment confirmed');
      queryClient.invalidateQueries({ queryKey: ['appointments'] });
    },
    onError: () => message.error('Failed to confirm appointment'),
  });

  const formatTime = (t: string) => t?.substring(0, 5) ?? '';

  const columns = [
    {
      title: 'Date',
      dataIndex: 'date',
      key: 'date',
      render: (d: string) => dayjs(d).format('DD/MM/YYYY'),
    },
    {
      title: 'Time',
      key: 'time',
      render: (_: unknown, r: Appointment) => `${formatTime(r.startTime)} – ${formatTime(r.endTime)}`,
    },
    {
      title: 'Doctor',
      dataIndex: 'doctorName',
      key: 'doctorName',
      render: (name: string | null) => name ?? '—',
    },
    {
      title: 'Patient',
      dataIndex: 'patientName',
      key: 'patientName',
      render: (name: string | null) => name ?? '—',
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (s: string) => <Tag color={STATUS_COLORS[s] ?? 'default'}>{s}</Tag>,
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 220,
      render: (_: unknown, record: Appointment) => (
        <Space size="small">
          {(isAdmin || isDoctor || isReceptionist) && record.status === 'PENDING' && (
            <Popconfirm
              title="Confirm this appointment?"
              onConfirm={() => confirmMutation.mutate(record.id)}
              okText="Yes"
              cancelText="No"
            >
              <Button size="small" type="primary">Confirm</Button>
            </Popconfirm>
          )}
          {(isAdmin || isPatient || isReceptionist) &&
            (record.status === 'PENDING' || record.status === 'CONFIRMED') && (
              <Popconfirm
                title="Cancel this appointment?"
                onConfirm={() => cancelMutation.mutate(record.id)}
                okText="Yes"
                cancelText="No"
              >
                <Button size="small" danger>Cancel</Button>
              </Popconfirm>
            )}
          {isDoctor && record.status === 'CONFIRMED' && (
            <Button
              size="small"
              style={{ borderColor: '#0D9488', color: '#0D9488' }}
              onClick={() =>
                navigate(
                  `/doctor/medical-records/create?patientId=${record.patientId}&patientName=${encodeURIComponent(record.patientName ?? '')}`
                )
              }
            >
              Create Record
            </Button>
          )}
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Title level={3} style={{ marginBottom: 24 }}>
        <CalendarOutlined style={{ marginRight: 8 }} />
        Appointments
      </Title>
      <Card style={{ borderRadius: 8 }}>
        <Space style={{ marginBottom: 16 }} wrap>
          <Select
            placeholder="Filter by status"
            style={{ width: 180 }}
            allowClear
            onChange={(val) => { setStatusFilter(val); setPage(1); }}
          >
            <Select.Option value="PENDING">Pending</Select.Option>
            <Select.Option value="CONFIRMED">Confirmed</Select.Option>
            <Select.Option value="COMPLETED">Completed</Select.Option>
            <Select.Option value="CANCELLED">Cancelled</Select.Option>
          </Select>
          <DatePicker
            placeholder="Filter by date"
            format="DD/MM/YYYY"
            onChange={(_, dateStr) => {
              const val = Array.isArray(dateStr) ? dateStr[0] : dateStr;
              setDateFilter(val ? dayjs(val, 'DD/MM/YYYY').format('YYYY-MM-DD') : undefined);
              setPage(1);
            }}
            allowClear
          />
        </Space>
        <Table
          dataSource={data?.data?.content ?? []}
          columns={columns}
          rowKey="id"
          loading={isLoading}
          pagination={{
            current: page,
            pageSize,
            total: data?.data?.totalElements ?? 0,
            onChange: (p, ps) => { setPage(p); setPageSize(ps); },
            showSizeChanger: true,
            pageSizeOptions: ['5', '10', '20', '50'],
            showTotal: (total) => `${total} appointments`,
          }}
        />
      </Card>
    </div>
  );
}
