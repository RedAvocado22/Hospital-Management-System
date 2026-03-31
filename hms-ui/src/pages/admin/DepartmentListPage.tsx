import { Table, Card, Button, Modal, Form, Input, Tag, Typography, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useState } from 'react';
import dayjs from 'dayjs';
import { getDepartments, createDepartment } from '../../api/departments';

const { Title } = Typography;

export default function DepartmentListPage() {
  const queryClient = useQueryClient();
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [createOpen, setCreateOpen] = useState(false);
  const [form] = Form.useForm();

  const { data, isLoading } = useQuery({
    queryKey: ['departments', page, pageSize],
    queryFn: () => getDepartments({ page: page - 1, size: pageSize }).then((r) => r.data),
  });

  const createMutation = useMutation({
    mutationFn: (values: { name: string }) => createDepartment(values),
    onSuccess: () => {
      message.success('Department created');
      setCreateOpen(false);
      form.resetFields();
      queryClient.invalidateQueries({ queryKey: ['departments'] });
    },
    onError: () => message.error('Failed to create department'),
  });

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      render: (name: string) => <strong>{name}</strong>,
    },
    {
      title: 'Status',
      dataIndex: 'isActive',
      key: 'isActive',
      render: (active: boolean) => <Tag color={active ? 'success' : 'error'}>{active ? 'Active' : 'Inactive'}</Tag>,
    },
    {
      title: 'Created At',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: (d: string) => d ? dayjs(d).format('DD/MM/YYYY') : '—',
    },
  ];

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <Title level={3} style={{ margin: 0 }}>Departments</Title>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => setCreateOpen(true)}>
          Create Department
        </Button>
      </div>
      <Card style={{ borderRadius: 8 }}>
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
            showTotal: (total) => `${total} departments`,
          }}
        />
      </Card>

      <Modal
        title="Create Department"
        open={createOpen}
        onCancel={() => { setCreateOpen(false); form.resetFields(); }}
        onOk={() => form.submit()}
        confirmLoading={createMutation.isPending}
      >
        <Form form={form} layout="vertical" onFinish={(vals) => createMutation.mutate(vals)}>
          <Form.Item name="name" label="Department Name" rules={[{ required: true, message: 'Name is required' }]}>
            <Input placeholder="e.g. Cardiology" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
