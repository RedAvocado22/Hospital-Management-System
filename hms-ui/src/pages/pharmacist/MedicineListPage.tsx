import {
  Table, Card, Input, Button, Space, Tag, Typography,
  Popconfirm, message, Divider, Modal, Form, InputNumber, Switch,
} from 'antd';
import { SearchOutlined, PlusOutlined, EditOutlined, StopOutlined } from '@ant-design/icons';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useState } from 'react';
import { getMedicines, createMedicine, updateMedicine, deactivateMedicine } from '../../api/medicines';
import type { Medicine } from '../../types';

const { Title } = Typography;

export default function MedicineListPage() {
  const queryClient = useQueryClient();
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [name, setName] = useState('');
  const [nameInput, setNameInput] = useState('');
  const [minPrice, setMinPrice] = useState<number | undefined>();
  const [maxPrice, setMaxPrice] = useState<number | undefined>();
  const [minPriceInput, setMinPriceInput] = useState<number | undefined>();
  const [maxPriceInput, setMaxPriceInput] = useState<number | undefined>();

  const [createOpen, setCreateOpen] = useState(false);
  const [editTarget, setEditTarget] = useState<Medicine | null>(null);

  const [createForm] = Form.useForm();
  const [editForm] = Form.useForm();

  const { data, isLoading } = useQuery({
    queryKey: ['medicines', page, pageSize, name, minPrice, maxPrice],
    queryFn: () =>
      getMedicines({ page: page - 1, size: pageSize, name, minPrice, maxPrice }).then((r) => r.data),
  });

  const createMutation = useMutation({
    mutationFn: createMedicine,
    onSuccess: () => {
      message.success('Medicine created');
      queryClient.invalidateQueries({ queryKey: ['medicines'] });
      setCreateOpen(false);
      createForm.resetFields();
    },
    onError: () => message.error('Failed to create medicine'),
  });

  const updateMutation = useMutation({
    mutationFn: updateMedicine,
    onSuccess: () => {
      message.success('Medicine updated');
      queryClient.invalidateQueries({ queryKey: ['medicines'] });
      setEditTarget(null);
    },
    onError: () => message.error('Failed to update medicine'),
  });

  const deactivateMutation = useMutation({
    mutationFn: deactivateMedicine,
    onSuccess: () => {
      message.success('Medicine deactivated');
      queryClient.invalidateQueries({ queryKey: ['medicines'] });
    },
    onError: () => message.error('Failed to deactivate medicine'),
  });

  const handleSearch = () => {
    setName(nameInput);
    setMinPrice(minPriceInput);
    setMaxPrice(maxPriceInput);
    setPage(1);
  };

  const handleReset = () => {
    setNameInput('');
    setMinPriceInput(undefined);
    setMaxPriceInput(undefined);
    setName('');
    setMinPrice(undefined);
    setMaxPrice(undefined);
    setPage(1);
  };

  const handleEditOpen = (record: Medicine) => {
    setEditTarget(record);
    editForm.setFieldsValue({
      name: record.name,
      description: record.description,
      price: record.price,
      quantity: record.quantity,
    });
  };

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      render: (name: string) => <strong>{name}</strong>,
    },
    {
      title: 'Description',
      dataIndex: 'description',
      key: 'description',
      ellipsis: true,
    },
    {
      title: 'Price',
      dataIndex: 'price',
      key: 'price',
      render: (price: number) =>
        price.toLocaleString('en-US', { style: 'currency', currency: 'USD' }),
    },
    {
      title: 'Quantity',
      dataIndex: 'quantity',
      key: 'quantity',
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
      render: (_: unknown, record: Medicine) => (
        <Space>
          <Button
            size="small"
            icon={<EditOutlined />}
            onClick={() => handleEditOpen(record)}
          />
          {record.isActive && (
            <Popconfirm
              title="Deactivate this medicine?"
              onConfirm={() => deactivateMutation.mutate(record.id)}
              okText="Yes"
              cancelText="No"
            >
              <Button size="small" danger icon={<StopOutlined />} />
            </Popconfirm>
          )}
        </Space>
      ),
    },
  ];

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <Title level={3} style={{ margin: 0 }}>Medicines</Title>
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => setCreateOpen(true)}
        >
          Add Medicine
        </Button>
      </div>

      <Card style={{ borderRadius: 8 }}>
        <Space style={{ marginBottom: 16 }} wrap>
          <Input
            placeholder="Search by name..."
            prefix={<SearchOutlined />}
            value={nameInput}
            onChange={(e) => setNameInput(e.target.value)}
            onPressEnter={handleSearch}
            style={{ width: 240 }}
            allowClear
            onClear={handleReset}
          />
          <InputNumber
            placeholder="Min price"
            value={minPriceInput}
            onChange={(val) => setMinPriceInput(val ?? undefined)}
            min={0}
            style={{ width: 120 }}
          />
          <InputNumber
            placeholder="Max price"
            value={maxPriceInput}
            onChange={(val) => setMaxPriceInput(val ?? undefined)}
            min={0}
            style={{ width: 120 }}
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
            pageSize: pageSize,
            total: data?.data?.totalElements ?? 0,
            onChange: (p, ps) => { setPage(p); setPageSize(ps); },
            showSizeChanger: true,
            pageSizeOptions: ['5', '10', '20', '50'],
            showTotal: (total) => `${total} medicines`,
          }}
        />
      </Card>

      {/* Create Modal */}
      <Modal
        title="Add Medicine"
        open={createOpen}
        onCancel={() => { setCreateOpen(false); createForm.resetFields(); }}
        onOk={() => createForm.submit()}
        okText="Create"
        confirmLoading={createMutation.isPending}
      >
        <Form
          form={createForm}
          layout="vertical"
          onFinish={(values) => createMutation.mutate(values)}
          initialValues={{ isActive: true }}
        >
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Name is required' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="description" label="Description" rules={[{ required: true, message: 'Description is required' }]}>
            <Input.TextArea rows={3} />
          </Form.Item>
          <Form.Item name="price" label="Price" rules={[{ required: true, message: 'Price is required' }]}>
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="quantity" label="Quantity" rules={[{ required: true, message: 'Quantity is required' }]}>
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="isActive" label="Active" valuePropName="checked">
            <Switch />
          </Form.Item>
        </Form>
      </Modal>

      {/* Edit Modal */}
      <Modal
        title="Edit Medicine"
        open={editTarget !== null}
        onCancel={() => setEditTarget(null)}
        onOk={() => editForm.submit()}
        okText="Save"
        confirmLoading={updateMutation.isPending}
      >
        <Form
          form={editForm}
          layout="vertical"
          onFinish={(values) =>
            updateMutation.mutate({ id: editTarget!.id, ...values })
          }
        >
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Name is required' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <Input.TextArea rows={3} />
          </Form.Item>
          <Form.Item name="price" label="Price">
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="quantity" label="Quantity">
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
