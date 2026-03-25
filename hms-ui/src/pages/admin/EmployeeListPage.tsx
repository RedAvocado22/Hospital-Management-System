import { Table, Card, Input, Button, Space, Tag, Typography, Select, Popconfirm, message, Divider } from 'antd';
import { SearchOutlined, PlusOutlined, CheckCircleOutlined, StopOutlined } from '@ant-design/icons';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getEmployees, deactivateEmployee, activateEmployee } from '../../api/employees';
import type { Employee } from '../../types';
import { ROLE_NAMES } from '../../constants/roles';

const { Title } = Typography;

export default function EmployeeListPage() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [page, setPage] = useState(1);
  const [keyword, setKeyword] = useState('');
  const [role, setRole] = useState<string | undefined>();
  const [searchInput, setSearchInput] = useState('');

  const { data, isLoading } = useQuery({
    queryKey: ['employees', page, keyword, role],
    queryFn: () => getEmployees({ page: page - 1, size: 10, keyword, role }).then((r) => r.data),
  });

  const deactivateMutation = useMutation({
    mutationFn: deactivateEmployee,
    onSuccess: () => {
      message.success('Employee deactivated');
      queryClient.invalidateQueries({ queryKey: ['employees'] });
    },
    onError: () => message.error('Failed to deactivate employee'),
  });

  const activateMutation = useMutation({
    mutationFn: activateEmployee,
    onSuccess: () => {
      message.success('Employee activated');
      queryClient.invalidateQueries({ queryKey: ['employees'] });
    },
    onError: () => message.error('Failed to activate employee'),
  });

  const columns = [
    {
      title: 'Code',
      dataIndex: 'code',
      key: 'code',
      width: 100,
    },
    {
      title: 'Full Name',
      dataIndex: 'fullName',
      key: 'fullName',
      render: (name: string) => <strong>{name}</strong>,
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Role',
      dataIndex: 'role',
      key: 'role',
      render: (role: { name: string }) => {
        const colorMap: Record<string, string> = {
          [ROLE_NAMES.DOCTOR]: 'blue',
          [ROLE_NAMES.RECEPTIONIST]: 'green',
          [ROLE_NAMES.PHARMACIST]: 'purple',
          [ROLE_NAMES.CASHIER]: 'orange',
          [ROLE_NAMES.ADMIN]: 'red',
        };
        return <Tag color={colorMap[role?.name] ?? 'default'}>{role?.name}</Tag>;
      },
    },
    {
      title: 'Department',
      dataIndex: 'department',
      key: 'department',
      render: (dept: { name: string }) => dept?.name ?? '-',
    },
    {
      title: 'Status',
      dataIndex: 'isActive',
      key: 'isActive',
      render: (active: boolean) => (
        <Tag color={active ? 'success' : 'error'}>{active ? 'Active' : 'Inactive'}</Tag>
      ),
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 120,
      render: (_: unknown, record: Employee) => (
        <Space>
          {record.isActive ? (
            <Popconfirm
              title="Deactivate this employee?"
              onConfirm={() => deactivateMutation.mutate(record.id)}
              okText="Yes"
              cancelText="No"
            >
              <Button size="small" danger icon={<StopOutlined />} />
            </Popconfirm>
          ) : (
            <Popconfirm
              title="Activate this employee?"
              onConfirm={() => activateMutation.mutate(record.id)}
              okText="Yes"
              cancelText="No"
            >
              <Button size="small" type="primary" icon={<CheckCircleOutlined />} />
            </Popconfirm>
          )}
        </Space>
      ),
    },
  ];

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <Title level={3} style={{ margin: 0 }}>Employees</Title>
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => navigate('/admin/employees/create')}
        >
          Add Employee
        </Button>
      </div>

      <Card style={{ borderRadius: 8 }}>
        <Space style={{ marginBottom: 16 }} wrap>
          <Input
            placeholder="Search by name, email..."
            prefix={<SearchOutlined />}
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
            onPressEnter={() => { setKeyword(searchInput); setPage(1); }}
            style={{ width: 280 }}
            allowClear
            onClear={() => { setKeyword(''); setSearchInput(''); setPage(1); }}
          />
          <Select
            placeholder="Filter by role"
            style={{ width: 180 }}
            allowClear
            onChange={(val) => { setRole(val); setPage(1); }}
          >
            <Select.Option value={ROLE_NAMES.DOCTOR}>Doctor</Select.Option>
            <Select.Option value={ROLE_NAMES.RECEPTIONIST}>Receptionist</Select.Option>
            <Select.Option value={ROLE_NAMES.PHARMACIST}>Pharmacist</Select.Option>
            <Select.Option value={ROLE_NAMES.CASHIER}>Cashier</Select.Option>
          </Select>
          <Button
            type="primary"
            icon={<SearchOutlined />}
            onClick={() => { setKeyword(searchInput); setPage(1); }}
          >
            Search
          </Button>
        </Space>

        <Divider style={{ margin: '0 0 16px 0' }} />
        <Table
          dataSource={data?.data?.content ?? []}
          columns={columns}
          rowKey="id"
          loading={isLoading}
          pagination={{
            current: page,
            pageSize: 10,
            total: data?.data?.totalElements ?? 0,
            onChange: (p) => setPage(p),
            showTotal: (total) => `${total} employees`,
          }}
        />
      </Card>
    </div>
  );
}
