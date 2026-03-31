import { Table, Card, Input, Button, Space, Tag, Typography, Divider } from 'antd';
import { SearchOutlined, EyeOutlined } from '@ant-design/icons';
import { useQuery } from '@tanstack/react-query';
import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { getPatients } from '../../api/patients';
import type { Patient } from '../../types';

const { Title } = Typography;

export default function PatientListPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const basePath = location.pathname.startsWith('/admin') ? '/admin' : '/receptionist';

  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [fullNameInput, setFullNameInput] = useState('');
  const [emailInput, setEmailInput] = useState('');
  const [phoneInput, setPhoneInput] = useState('');

  const { data, isLoading } = useQuery({
    queryKey: ['patients', page, pageSize, fullName, email, phone],
    queryFn: () => getPatients({ page: page - 1, size: pageSize, fullName, email, phone }).then((r) => r.data),
  });

  const handleSearch = () => {
    setFullName(fullNameInput);
    setEmail(emailInput);
    setPhone(phoneInput);
    setPage(1);
  };

  const handleReset = () => {
    setFullNameInput(''); setEmailInput(''); setPhoneInput('');
    setFullName(''); setEmail(''); setPhone('');
    setPage(1);
  };

  const columns = [
    {
      title: 'Full Name',
      dataIndex: 'fullName',
      key: 'fullName',
      render: (name: string) => <strong>{name}</strong>,
    },
    { title: 'Email', dataIndex: 'email', key: 'email' },
    { title: 'Phone', dataIndex: 'phone', key: 'phone' },
    {
      title: 'Status',
      dataIndex: 'active',
      key: 'active',
      render: (active: boolean) => <Tag color={active ? 'success' : 'error'}>{active ? 'Active' : 'Inactive'}</Tag>,
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 80,
      render: (_: unknown, record: Patient) => (
        <Button
          size="small"
          icon={<EyeOutlined />}
          onClick={() => navigate(`${basePath}/patients/${record.id}`)}
        >
          View
        </Button>
      ),
    },
  ];

  return (
    <div>
      <Title level={3} style={{ marginBottom: 24 }}>Patients</Title>
      <Card style={{ borderRadius: 8 }}>
        <Space style={{ marginBottom: 16 }} wrap>
          <Input
            placeholder="Search by name"
            prefix={<SearchOutlined />}
            value={fullNameInput}
            onChange={(e) => setFullNameInput(e.target.value)}
            onPressEnter={handleSearch}
            style={{ width: 200 }}
            allowClear
          />
          <Input
            placeholder="Search by email"
            value={emailInput}
            onChange={(e) => setEmailInput(e.target.value)}
            onPressEnter={handleSearch}
            style={{ width: 200 }}
            allowClear
          />
          <Input
            placeholder="Search by phone"
            value={phoneInput}
            onChange={(e) => setPhoneInput(e.target.value)}
            onPressEnter={handleSearch}
            style={{ width: 180 }}
            allowClear
          />
          <Button type="primary" icon={<SearchOutlined />} onClick={handleSearch}>Search</Button>
          <Button onClick={handleReset}>Reset</Button>
        </Space>
        <Divider style={{ margin: '0 0 16px 0' }} />
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
            showTotal: (total) => `${total} patients`,
          }}
        />
      </Card>
    </div>
  );
}
