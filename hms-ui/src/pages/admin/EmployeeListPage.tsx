import { Table, Card, Input, Button, Space, Tag, Typography, Select, Popconfirm, message, Divider, Modal, Form, DatePicker } from 'antd';
import { SearchOutlined, PlusOutlined, CheckCircleOutlined, StopOutlined, EditOutlined } from '@ant-design/icons';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import dayjs from 'dayjs';
import { getEmployees, deactivateEmployee, activateEmployee, updateEmployee } from '../../api/employees';
import { getDepartments } from '../../api/departments';
import type { Employee, UpdateEmployeeRequest } from '../../types';
import { ROLE_NAMES } from '../../constants/roles';

const { Title } = Typography;

export default function EmployeeListPage() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [name, setName] = useState('');
  const [role, setRole] = useState<string | undefined>();
  const [department, setDepartment] = useState<string | undefined>();
  const [isActive, setIsActive] = useState<boolean | undefined>();
  const [hireDateFrom, setHireDateFrom] = useState<string | undefined>();
  const [hireDateTo, setHireDateTo] = useState<string | undefined>();
  const [searchInput, setSearchInput] = useState('');

  const [editOpen, setEditOpen] = useState(false);
  const [editingEmployee, setEditingEmployee] = useState<Employee | null>(null);
  const [editForm] = Form.useForm();

  const { data, isLoading } = useQuery({
    queryKey: ['employees', page, pageSize, name, role, department, isActive, hireDateFrom, hireDateTo],
    queryFn: () => getEmployees({ page: page - 1, size: pageSize, name, roleName: role, department, isActive, hireDateFrom, hireDateTo }).then((r) => r.data),
  });

  const { data: deptData } = useQuery({
    queryKey: ['departments-all'],
    queryFn: () => getDepartments({ page: 0, size: 100 }).then((r) => r.data),
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

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: UpdateEmployeeRequest }) => updateEmployee(id, data),
    onSuccess: () => {
      message.success('Employee updated');
      setEditOpen(false);
      setEditingEmployee(null);
      editForm.resetFields();
      queryClient.invalidateQueries({ queryKey: ['employees'] });
    },
    onError: () => message.error('Failed to update employee'),
  });

  const openEdit = (record: Employee) => {
    setEditingEmployee(record);
    editForm.setFieldsValue({
      firstName: record.firstName,
      lastName: record.lastName,
      email: record.email,
      phone: record.phone,
      dob: record.dob ? dayjs(record.dob) : undefined,
      gender: record.gender,
      address: record.address,
      role: record.role,
      department: record.department?.name,
      hireDate: record.hireDate ? dayjs(record.hireDate) : undefined,
    });
    setEditOpen(true);
  };

  const handleEditSubmit = (values: Record<string, unknown>) => {
    if (!editingEmployee) return;
    const payload: UpdateEmployeeRequest = {};
    if (values.firstName) payload.firstName = values.firstName as string;
    if (values.lastName) payload.lastName = values.lastName as string;
    if (values.email) payload.email = values.email as string;
    if (values.phone) payload.phone = values.phone as string;
    if (values.dob) payload.dob = dayjs(values.dob as string).format('YYYY-MM-DD');
    if (values.gender) payload.gender = values.gender as string;
    if (values.address) payload.address = values.address as string;
    if (values.role) payload.role = values.role as string;
    if (values.department) payload.department = values.department as string;
    if (values.hireDate) payload.hireDate = dayjs(values.hireDate as string).format('YYYY-MM-DD');
    if (values.password) payload.password = values.password as string;
    updateMutation.mutate({ id: editingEmployee.id, data: payload });
  };

  const departments = deptData?.data?.content ?? [];

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
      render: (role: string) => {
        const colorMap: Record<string, string> = {
          [ROLE_NAMES.DOCTOR]: 'blue',
          [ROLE_NAMES.RECEPTIONIST]: 'green',
          [ROLE_NAMES.PHARMACIST]: 'purple',
          [ROLE_NAMES.CASHIER]: 'orange',
          [ROLE_NAMES.ADMIN]: 'red',
        };
        return <Tag color={colorMap[role] ?? 'default'}>{role}</Tag>;
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
      width: 160,
      render: (_: unknown, record: Employee) => (
        <Space>
          <Button
            size="small"
            icon={<EditOutlined />}
            onClick={() => openEdit(record)}
          />
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
            onPressEnter={() => { setName(searchInput); setPage(1); }}
            style={{ width: 280 }}
            allowClear
            onClear={() => { setName(''); setSearchInput(''); setPage(1); }}
          />
          <Select
            placeholder="Filter by role"
            style={{ width: 160 }}
            allowClear
            onChange={(val) => { setRole(val); setPage(1); }}
          >
            <Select.Option value={ROLE_NAMES.DOCTOR}>Doctor</Select.Option>
            <Select.Option value={ROLE_NAMES.RECEPTIONIST}>Receptionist</Select.Option>
            <Select.Option value={ROLE_NAMES.PHARMACIST}>Pharmacist</Select.Option>
            <Select.Option value={ROLE_NAMES.CASHIER}>Cashier</Select.Option>
            <Select.Option value={ROLE_NAMES.ADMIN}>Admin</Select.Option>
          </Select>
          <Select
            placeholder="Filter by department"
            style={{ width: 180 }}
            allowClear
            onChange={(val) => { setDepartment(val); setPage(1); }}
          >
            {departments.map((d) => (
              <Select.Option key={d.id} value={d.name}>{d.name}</Select.Option>
            ))}
          </Select>
          <Select
            placeholder="Filter by status"
            style={{ width: 140 }}
            allowClear
            onChange={(val) => { setIsActive(val); setPage(1); }}
          >
            <Select.Option value={true}>Active</Select.Option>
            <Select.Option value={false}>Inactive</Select.Option>
          </Select>
          <DatePicker.RangePicker
            placeholder={['Hire date from', 'Hire date to']}
            format="DD/MM/YYYY"
            onChange={(dates, dateStrings) => {
              if (!dates) {
                setHireDateFrom(undefined);
                setHireDateTo(undefined);
              } else {
                setHireDateFrom(dateStrings[0] ? dayjs(dateStrings[0], 'DD/MM/YYYY').format('YYYY-MM-DD') : undefined);
                setHireDateTo(dateStrings[1] ? dayjs(dateStrings[1], 'DD/MM/YYYY').format('YYYY-MM-DD') : undefined);
              }
              setPage(1);
            }}
          />
          <Button
            type="primary"
            icon={<SearchOutlined />}
            onClick={() => { setName(searchInput); setPage(1); }}
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
            pageSize: pageSize,
            total: data?.data?.totalElements ?? 0,
            onChange: (p, ps) => { setPage(p); setPageSize(ps); },
            showSizeChanger: true,
            pageSizeOptions: ['5', '10', '20', '50'],
            showTotal: (total) => `${total} employees`,
          }}
        />
      </Card>

      <Modal
        title="Edit Employee"
        open={editOpen}
        onCancel={() => { setEditOpen(false); setEditingEmployee(null); editForm.resetFields(); }}
        onOk={() => editForm.submit()}
        confirmLoading={updateMutation.isPending}
        width={640}
      >
        <Form form={editForm} layout="vertical" onFinish={handleEditSubmit}>
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
          <Form.Item name="role" label="Role">
            <Select allowClear>
              <Select.Option value={ROLE_NAMES.DOCTOR}>Doctor</Select.Option>
              <Select.Option value={ROLE_NAMES.RECEPTIONIST}>Receptionist</Select.Option>
              <Select.Option value={ROLE_NAMES.PHARMACIST}>Pharmacist</Select.Option>
              <Select.Option value={ROLE_NAMES.CASHIER}>Cashier</Select.Option>
              <Select.Option value={ROLE_NAMES.ADMIN}>Admin</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="department" label="Department">
            <Select allowClear>
              {departments.map((d) => (
                <Select.Option key={d.id} value={d.name}>{d.name}</Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item name="hireDate" label="Hire Date">
            <DatePicker style={{ width: '100%' }} format="DD/MM/YYYY" />
          </Form.Item>
          <Form.Item name="password" label="New Password (leave blank to keep current)">
            <Input.Password />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
