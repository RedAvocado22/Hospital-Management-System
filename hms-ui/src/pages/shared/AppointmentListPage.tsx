import {
    Table,
    Card,
    Select,
    Space,
    Tag,
    Button,
    Popconfirm,
    message,
    Typography,
    DatePicker,
    Modal,
    Form,
    Input,
    Spin,
    Empty,
} from "antd";
import { CalendarOutlined, PlusOutlined, UserOutlined } from "@ant-design/icons";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import dayjs from "dayjs";
import {
    getAppointments,
    bookAppointment,
    cancelAppointment,
    confirmAppointment,
} from "../../api/appointments";
import { getDoctorSchedules } from "../../api/doctorSchedules";
import { useAuthStore } from "../../store/authStore";
import type { Appointment, DoctorSchedule } from "../../types";

const SHIFT_CONFIG = {
    MORNING: { label: "Morning", time: "07:00 – 12:00", color: "#1890FF", bg: "#E6F4FF", borderColor: "#91CAFF" },
    AFTERNOON: { label: "Afternoon", time: "13:00 – 18:00", color: "#FA8C16", bg: "#FFF7E6", borderColor: "#FFD591" },
    EVENING: { label: "Evening", time: "19:00 – 22:00", color: "#722ED1", bg: "#F9F0FF", borderColor: "#D3ADF7" },
} as const;

const { Title } = Typography;

const STATUS_COLORS: Record<string, string> = {
    PENDING: "orange",
    CONFIRMED: "blue",
    COMPLETED: "success",
    CANCELLED: "red",
};

export default function AppointmentListPage() {
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const { user } = useAuthStore();
    const [page, setPage] = useState(1);
    const [pageSize, setPageSize] = useState(10);
    const [statusFilter, setStatusFilter] = useState<string | undefined>();
    const [dateFilter, setDateFilter] = useState<string | undefined>();
    const [bookOpen, setBookOpen] = useState(false);
    const [bookForm] = Form.useForm();
    const [bookDate, setBookDate] = useState<string | undefined>();
    const [selectedScheduleId, setSelectedScheduleId] = useState<string | undefined>();

    const isAdmin = user?.roles?.includes("ROLE_ADMIN") ?? false;
    const isDoctor = user?.roles?.includes("ROLE_DOCTOR") ?? false;
    const isReceptionist = user?.roles?.includes("ROLE_RECEPTIONIST") ?? false;
    const isPatient = user?.roles?.includes("ROLE_PATIENT") ?? false;

    const { data, isLoading } = useQuery({
        queryKey: ["appointments", page, pageSize, statusFilter, dateFilter],
        queryFn: () =>
            getAppointments({
                page: page - 1,
                size: pageSize,
                status: statusFilter,
                date: dateFilter,
            }).then((r) => r.data),
    });

    const cancelMutation = useMutation({
        mutationFn: cancelAppointment,
        onSuccess: () => {
            message.success("Appointment cancelled");
            queryClient.invalidateQueries({ queryKey: ["appointments"] });
        },
        onError: () => message.error("Failed to cancel appointment"),
    });

    const confirmMutation = useMutation({
        mutationFn: confirmAppointment,
        onSuccess: () => {
            message.success("Appointment confirmed");
            queryClient.invalidateQueries({ queryKey: ["appointments"] });
        },
        onError: () => message.error("Failed to confirm appointment"),
    });

    const { data: schedulesData, isLoading: schedulesLoading } = useQuery({
        queryKey: ["book-schedules", bookDate],
        queryFn: () =>
            getDoctorSchedules({ date: bookDate, size: 50 }).then((r) => r.data),
        enabled: !!bookDate && bookOpen,
    });
    const availableSchedules = (schedulesData?.data?.content ?? []).filter(
        (s: DoctorSchedule) => s.isAvailable,
    );

    const bookMutation = useMutation({
        mutationFn: bookAppointment,
        onSuccess: () => {
            message.success("Appointment booked");
            setBookOpen(false);
            bookForm.resetFields();
            setBookDate(undefined);
            setSelectedScheduleId(undefined);
            queryClient.invalidateQueries({ queryKey: ["appointments"] });
        },
        onError: (err: unknown) => {
            const msg =
                (err as { response?: { data?: { message?: string } } })
                    ?.response?.data?.message ?? "Failed to book appointment";
            message.error(msg);
        },
    });

    const handleBook = (values: { reason: string; patientId?: string }) => {
        if (!selectedScheduleId) {
            message.warning("Please select a schedule");
            return;
        }
        bookMutation.mutate({
            scheduleId: selectedScheduleId,
            reason: values.reason,
            patientId: isReceptionist ? values.patientId : undefined,
        });
    };

    const handleBookModalClose = () => {
        setBookOpen(false);
        bookForm.resetFields();
        setBookDate(undefined);
        setSelectedScheduleId(undefined);
    };

    const formatTime = (t: string) => t?.substring(0, 5) ?? "";

    const columns = [
        {
            title: "Date",
            dataIndex: "date",
            key: "date",
            render: (d: string) => dayjs(d).format("DD/MM/YYYY"),
        },
        {
            title: "Time",
            key: "time",
            render: (_: unknown, r: Appointment) =>
                `${formatTime(r.startTime)} – ${formatTime(r.endTime)}`,
        },
        {
            title: "Doctor",
            dataIndex: "doctorName",
            key: "doctorName",
            render: (name: string | null) => name ?? "—",
        },
        {
            title: "Patient",
            dataIndex: "patientName",
            key: "patientName",
            render: (name: string | null) => name ?? "—",
        },
        {
            title: "Status",
            dataIndex: "status",
            key: "status",
            render: (s: string) => (
                <Tag color={STATUS_COLORS[s] ?? "default"}>{s}</Tag>
            ),
        },
        {
            title: "Actions",
            key: "actions",
            width: 220,
            render: (_: unknown, record: Appointment) => (
                <Space size="small">
                    {(isAdmin || isDoctor || isReceptionist) &&
                        record.status === "PENDING" && (
                            <Popconfirm
                                title="Confirm this appointment?"
                                onConfirm={() =>
                                    confirmMutation.mutate(record.id)
                                }
                                okText="Yes"
                                cancelText="No"
                            >
                                <Button size="small" type="primary">
                                    Confirm
                                </Button>
                            </Popconfirm>
                        )}
                    {(isAdmin || isPatient || isReceptionist) &&
                        (record.status === "PENDING" ||
                            record.status === "CONFIRMED") && (
                            <Popconfirm
                                title="Cancel this appointment?"
                                onConfirm={() =>
                                    cancelMutation.mutate(record.id)
                                }
                                okText="Yes"
                                cancelText="No"
                            >
                                <Button size="small" danger>
                                    Cancel
                                </Button>
                            </Popconfirm>
                        )}
                    {isDoctor && (
                        <Button
                            size="small"
                            icon={<UserOutlined />}
                            onClick={() =>
                                navigate(`/doctor/patients/${record.patientId}`)
                            }
                        >
                            Patient
                        </Button>
                    )}
                    {isDoctor && record.status === "CONFIRMED" && (
                        <Button
                            size="small"
                            style={{ borderColor: "#0D9488", color: "#0D9488" }}
                            onClick={() =>
                                navigate(
                                    `/doctor/medical-records/create?patientId=${record.patientId}&patientName=${encodeURIComponent(record.patientName ?? "")}`,
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
            <div
                style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    marginBottom: 24,
                }}
            >
                <Title level={3} style={{ margin: 0 }}>
                    <CalendarOutlined style={{ marginRight: 8 }} />
                    Appointments
                </Title>
                {(isPatient || isReceptionist) && (
                    <Button
                        type="primary"
                        icon={<PlusOutlined />}
                        onClick={() => setBookOpen(true)}
                    >
                        Book Appointment
                    </Button>
                )}
            </div>

            <Card style={{ borderRadius: 8 }}>
                <Space style={{ marginBottom: 16 }} wrap>
                    <Select
                        placeholder="Filter by status"
                        style={{ width: 180 }}
                        allowClear
                        onChange={(val) => {
                            setStatusFilter(val);
                            setPage(1);
                        }}
                    >
                        <Select.Option value="PENDING">Pending</Select.Option>
                        <Select.Option value="CONFIRMED">
                            Confirmed
                        </Select.Option>
                        <Select.Option value="COMPLETED">
                            Completed
                        </Select.Option>
                        <Select.Option value="CANCELLED">
                            Cancelled
                        </Select.Option>
                    </Select>
                    <DatePicker
                        placeholder="Filter by date"
                        format="DD/MM/YYYY"
                        onChange={(_, dateStr) => {
                            const val = Array.isArray(dateStr)
                                ? dateStr[0]
                                : dateStr;
                            setDateFilter(
                                val
                                    ? dayjs(val, "DD/MM/YYYY").format(
                                          "YYYY-MM-DD",
                                      )
                                    : undefined,
                            );
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
                        onChange: (p, ps) => {
                            setPage(p);
                            setPageSize(ps);
                        },
                        showSizeChanger: true,
                        pageSizeOptions: ["5", "10", "20", "50"],
                        showTotal: (total) => `${total} appointments`,
                    }}
                />
            </Card>

            <Modal
                title="Book Appointment"
                open={bookOpen}
                onCancel={handleBookModalClose}
                onOk={() => bookForm.submit()}
                confirmLoading={bookMutation.isPending}
                width={560}
                okText="Book"
                okButtonProps={{ disabled: !selectedScheduleId }}
            >
                <Form
                    form={bookForm}
                    layout="vertical"
                    onFinish={handleBook}
                    style={{ marginTop: 8 }}
                >
                    {isReceptionist && (
                        <Form.Item
                            name="patientId"
                            label="Patient ID"
                            rules={[{ required: true, message: "Patient ID is required" }]}
                        >
                            <Input placeholder="Patient UUID" />
                        </Form.Item>
                    )}

                    <Form.Item label="Appointment Date">
                        <DatePicker
                            style={{ width: "100%" }}
                            format="DD/MM/YYYY"
                            disabledDate={(d) => d.isBefore(dayjs(), "day")}
                            onChange={(_, dateStr) => {
                                const val = Array.isArray(dateStr) ? dateStr[0] : dateStr;
                                setBookDate(
                                    val
                                        ? dayjs(val, "DD/MM/YYYY").format("YYYY-MM-DD")
                                        : undefined,
                                );
                                setSelectedScheduleId(undefined);
                            }}
                        />
                    </Form.Item>

                    {bookDate && (
                        <Form.Item label="Available Schedules">
                            {schedulesLoading ? (
                                <div style={{ textAlign: "center", padding: 16 }}>
                                    <Spin size="small" />
                                </div>
                            ) : availableSchedules.length === 0 ? (
                                <Empty
                                    description="No available schedules on this date"
                                    image={Empty.PRESENTED_IMAGE_SIMPLE}
                                />
                            ) : (
                                <div style={{ display: "flex", flexWrap: "wrap", gap: 10 }}>
                                    {availableSchedules.map((s: DoctorSchedule) => {
                                        const shift = SHIFT_CONFIG[s.type];
                                        const isSelected = selectedScheduleId === s.id;
                                        return (
                                            <div
                                                key={s.id}
                                                onClick={() => setSelectedScheduleId(s.id)}
                                                style={{
                                                    flex: "1 1 calc(50% - 5px)",
                                                    minWidth: 200,
                                                    padding: "12px 14px",
                                                    borderRadius: 8,
                                                    border: `2px solid ${isSelected ? shift.color : "#e5e9ef"}`,
                                                    background: isSelected ? shift.bg : "#fff",
                                                    cursor: "pointer",
                                                    transition: "all 0.2s",
                                                    boxShadow: isSelected
                                                        ? `0 0 0 2px ${shift.color}22`
                                                        : "none",
                                                }}
                                            >
                                                <div style={{ marginBottom: 4 }}>
                                                    <Tag
                                                        color={shift.color}
                                                        style={{ fontWeight: 600, fontSize: 12 }}
                                                    >
                                                        {shift.label}
                                                    </Tag>
                                                    <span style={{ fontSize: 12, color: "#8c8c8c" }}>
                                                        {shift.time}
                                                    </span>
                                                </div>
                                                <div style={{ fontWeight: 500, fontSize: 13 }}>
                                                    {s.doctorName}
                                                </div>
                                                <div style={{ fontSize: 12, color: "#8c8c8c", marginTop: 2 }}>
                                                    {s.maxPatients} max patients
                                                </div>
                                            </div>
                                        );
                                    })}
                                </div>
                            )}
                        </Form.Item>
                    )}

                    <Form.Item
                        name="reason"
                        label="Reason"
                        rules={[{ required: true, message: "Reason is required" }]}
                    >
                        <Input.TextArea rows={3} placeholder="Reason for appointment" />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
}
