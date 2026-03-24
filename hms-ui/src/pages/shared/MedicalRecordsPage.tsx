import { Table, Card, Input, Button, Space, Typography, DatePicker, Divider } from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import { useQuery } from '@tanstack/react-query';
import { useState } from 'react';
import { getMedicalRecords } from '../../api/medicalRecords';
import type { MedicalRecord } from '../../types';
import dayjs from 'dayjs';

const { Title, Text } = Typography;
const { RangePicker } = DatePicker;

export default function MedicalRecordsPage() {
  const [page, setPage] = useState(1);
  const [keyword, setKeyword] = useState('');
  const [doctorName, setDoctorName] = useState('');
  const [dateRange, setDateRange] = useState<[string, string] | null>(null);
  const [searchInput, setSearchInput] = useState('');
  const [doctorInput, setDoctorInput] = useState('');

  const { data, isLoading } = useQuery({
    queryKey: ['medical-records', page, keyword, doctorName, dateRange],
    queryFn: () =>
      getMedicalRecords({
        page: page - 1,
        size: 10,
        keyword: keyword || undefined,
        doctorName: doctorName || undefined,
        from: dateRange?.[0] || undefined,
        to: dateRange?.[1] || undefined,
      }).then((r) => r.data),
  });

  const columns = [
    {
      title: 'Patient',
      dataIndex: 'patientName',
      key: 'patientName',
      render: (name: string) => <strong>{name}</strong>,
    },
    {
      title: 'Doctor',
      dataIndex: 'doctorName',
      key: 'doctorName',
    },
    {
      title: 'Description',
      dataIndex: 'description',
      key: 'description',
      ellipsis: true,
      render: (text: string) => <Text type="secondary">{text}</Text>,
    },
    {
      title: 'Doctor Advice',
      dataIndex: 'doctorAdvice',
      key: 'doctorAdvice',
      ellipsis: true,
    },
    {
      title: 'Date',
      dataIndex: 'createdAt',
      key: 'createdAt',
      width: 160,
      render: (date: string) => dayjs(date).format('DD/MM/YYYY HH:mm'),
    },
  ];

  const handleSearch = () => {
    setKeyword(searchInput);
    setDoctorName(doctorInput);
    setPage(1);
  };

  const handleReset = () => {
    setSearchInput('');
    setDoctorInput('');
    setKeyword('');
    setDoctorName('');
    setDateRange(null);
    setPage(1);
  };

  return (
    <div>
      <Title level={3} style={{ marginBottom: 24 }}>Medical Records</Title>

      <Card style={{ borderRadius: 8 }}>
        <Space style={{ marginBottom: 16 }} wrap>
          <Input
            placeholder="Search patient..."
            prefix={<SearchOutlined />}
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
            onPressEnter={handleSearch}
            style={{ width: 220 }}
            allowClear
          />
          <Input
            placeholder="Doctor name..."
            value={doctorInput}
            onChange={(e) => setDoctorInput(e.target.value)}
            onPressEnter={handleSearch}
            style={{ width: 200 }}
            allowClear
          />
          <RangePicker
            onChange={(_, dateStrings) => {
              if (dateStrings[0] && dateStrings[1]) {
                setDateRange([dateStrings[0], dateStrings[1]]);
              } else {
                setDateRange(null);
              }
            }}
          />
          <Button type="primary" icon={<SearchOutlined />} onClick={handleSearch}>
            Search
          </Button>
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
            pageSize: 10,
            total: data?.data?.totalElements ?? 0,
            onChange: (p) => setPage(p),
            showTotal: (total) => `${total} records`,
          }}
        />
      </Card>
    </div>
  );
}
