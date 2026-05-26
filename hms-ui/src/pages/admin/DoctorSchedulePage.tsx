import {
    Calendar,
    Card,
    Badge,
    Button,
    Modal,
    Form,
    Select,
    InputNumber,
    Space,
    Typography,
    Tag,
    Spin,
    Popconfirm,
    message,
    Empty,
    Row,
    Col,
} from "antd";
import {
    PlusOutlined,
    ClockCircleOutlined,
    UserOutlined,
    StopOutlined,
} from "@ant-design/icons";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import dayjs, { type Dayjs } from "dayjs";
import {
    getDoctorSchedules,
    createDoctorSchedule,
    deactivateDoctorSchedule,
} from "../../api/doctorSchedules";
import { getEmployees } from "../../api/employees";
import type { DoctorSchedule } from "../../types";

const { Title, Text } = Typography;

const SHIFT_CONFIG = {
    MORNING: {
        label: "Morning",
        time: "07:00 – 12:00",
        color: "#1890FF",
        bg: "#E6F4FF",
        borderColor: "#91CAFF",
    },
    AFTERNOON: {
        label: "Afternoon",
        time: "13:00 – 18:00",
        color: "#FA8C16",
        bg: "#FFF7E6",
        borderColor: "#FFD591",
    },
    EVENING: {
        label: "Evening",
        time: "19:00 – 22:00",
        color: "#722ED1",
        bg: "#F9F0FF",
        borderColor: "#D3ADF7",
    },
} as const;

export default function DoctorSchedulePage() {
    const queryClient = useQueryClient();
    const [selectedDate, setSelectedDate] = useState<Dayjs>(dayjs());
    const [createOpen, setCreateOpen] = useState(false);
    const [form] = Form.useForm();

    const dateStr = selectedDate.format("YYYY-MM-DD");

    const { data: schedulesData, isLoading: schedulesLoading } = useQuery({
        queryKey: ["doctor-schedules", dateStr],
        queryFn: () =>
            getDoctorSchedules({ date: dateStr, size: 50 }).then(
                (r) => r.data,
            ),
    });

    const { data: doctorsData } = useQuery({
        queryKey: ["doctors-list"],
        queryFn: () =>
            getEmployees({ roleName: "DOCTOR", isActive: true, size: 100 }).then(
                (r) => r.data,
            ),
        staleTime: 60_000,
    });

    const schedules = schedulesData?.data?.content ?? [];
    const doctors = doctorsData?.data?.content ?? [];

    const createMutation = useMutation({
        mutationFn: createDoctorSchedule,
        onSuccess: () => {
            message.success("Schedule created");
            setCreateOpen(false);
            form.resetFields();
            queryClient.invalidateQueries({ queryKey: ["doctor-schedules"] });
        },
        onError: (err: unknown) => {
            const msg =
                (err as { response?: { data?: { message?: string } } })
                    ?.response?.data?.message ?? "Failed to create schedule";
            message.error(msg);
        },
    });

    const deactivateMutation = useMutation({
        mutationFn: deactivateDoctorSchedule,
        onSuccess: () => {
            message.success("Schedule deactivated");
            queryClient.invalidateQueries({ queryKey: ["doctor-schedules"] });
        },
        onError: (err: unknown) => {
            const msg =
                (err as { response?: { data?: { message?: string } } })
                    ?.response?.data?.message ?? "Failed to deactivate";
            message.error(msg);
        },
    });

    const handleCreate = (values: {
        doctorId: string;
        type: string;
        maxPatients: number;
    }) => {
        createMutation.mutate({
            doctorId: values.doctorId,
            date: dateStr,
            type: values.type,
            maxPatients: values.maxPatients,
        });
    };

    const openCreate = () => {
        form.resetFields();
        setCreateOpen(true);
    };

    const cellRender = (date: Dayjs) => {
        const isToday = date.isSame(dayjs(), "day");
        const isSelected = date.isSame(selectedDate, "day");
        if (isSelected && !isToday) {
            return (
                <div
                    style={{
                        width: 6,
                        height: 6,
                        borderRadius: "50%",
                        background: "#0D9488",
                        margin: "2px auto 0",
                    }}
                />
            );
        }
        return null;
    };

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
                    Doctor Schedules
                </Title>
            </div>

            <Row gutter={24}>
                {/* Calendar */}
                <Col xs={24} lg={14}>
                    <Card style={{ borderRadius: 8 }}>
                        <Calendar
                            fullscreen={false}
                            value={selectedDate}
                            onSelect={(date) => setSelectedDate(date)}
                            cellRender={cellRender}
                            style={{ border: "none" }}
                        />
                    </Card>
                </Col>

                {/* Schedule panel */}
                <Col xs={24} lg={10}>
                    <Card
                        style={{ borderRadius: 8, minHeight: 400 }}
                        title={
                            <Space>
                                <ClockCircleOutlined style={{ color: "#0D9488" }} />
                                <Text strong>
                                    {selectedDate.format("dddd, DD/MM/YYYY")}
                                </Text>
                            </Space>
                        }
                        extra={
                            <Button
                                type="primary"
                                icon={<PlusOutlined />}
                                size="small"
                                onClick={openCreate}
                            >
                                Add Schedule
                            </Button>
                        }
                    >
                        {schedulesLoading ? (
                            <div style={{ textAlign: "center", padding: 40 }}>
                                <Spin />
                            </div>
                        ) : schedules.length === 0 ? (
                            <Empty
                                description="No schedules for this day"
                                image={Empty.PRESENTED_IMAGE_SIMPLE}
                            />
                        ) : (
                            <Space direction="vertical" style={{ width: "100%" }}>
                                {schedules.map((s: DoctorSchedule) => {
                                    const shift = SHIFT_CONFIG[s.type];
                                    return (
                                        <div
                                            key={s.id}
                                            style={{
                                                padding: "12px 14px",
                                                borderRadius: 8,
                                                border: `1px solid ${s.isAvailable ? shift.borderColor : "#d9d9d9"}`,
                                                background: s.isAvailable
                                                    ? shift.bg
                                                    : "#fafafa",
                                                opacity: s.isAvailable ? 1 : 0.6,
                                            }}
                                        >
                                            <div
                                                style={{
                                                    display: "flex",
                                                    justifyContent: "space-between",
                                                    alignItems: "flex-start",
                                                }}
                                            >
                                                <div>
                                                    <Space size={6}>
                                                        <Badge
                                                            color={shift.color}
                                                            text={
                                                                <Text
                                                                    strong
                                                                    style={{
                                                                        color: shift.color,
                                                                        fontSize: 13,
                                                                    }}
                                                                >
                                                                    {shift.label}
                                                                </Text>
                                                            }
                                                        />
                                                        <Text
                                                            type="secondary"
                                                            style={{ fontSize: 12 }}
                                                        >
                                                            {shift.time}
                                                        </Text>
                                                    </Space>
                                                    <div
                                                        style={{
                                                            marginTop: 6,
                                                            display: "flex",
                                                            alignItems: "center",
                                                            gap: 6,
                                                        }}
                                                    >
                                                        <UserOutlined
                                                            style={{
                                                                color: "#8c8c8c",
                                                                fontSize: 12,
                                                            }}
                                                        />
                                                        <Text style={{ fontSize: 13 }}>
                                                            {s.doctorName}
                                                        </Text>
                                                    </div>
                                                    <div style={{ marginTop: 4 }}>
                                                        <Text
                                                            type="secondary"
                                                            style={{ fontSize: 12 }}
                                                        >
                                                            Max {s.maxPatients} patients
                                                        </Text>
                                                        <Tag
                                                            style={{
                                                                marginLeft: 8,
                                                                fontSize: 11,
                                                            }}
                                                            color={
                                                                s.isAvailable
                                                                    ? "success"
                                                                    : "default"
                                                            }
                                                        >
                                                            {s.isAvailable
                                                                ? "Active"
                                                                : "Inactive"}
                                                        </Tag>
                                                    </div>
                                                </div>

                                                {s.isAvailable && (
                                                    <Popconfirm
                                                        title="Deactivate this schedule?"
                                                        description="Patients with active appointments will be affected."
                                                        onConfirm={() =>
                                                            deactivateMutation.mutate(
                                                                s.id,
                                                            )
                                                        }
                                                        okText="Deactivate"
                                                        okButtonProps={{
                                                            danger: true,
                                                        }}
                                                        cancelText="Cancel"
                                                    >
                                                        <Button
                                                            size="small"
                                                            danger
                                                            icon={<StopOutlined />}
                                                            loading={
                                                                deactivateMutation.isPending
                                                            }
                                                        >
                                                            Deactivate
                                                        </Button>
                                                    </Popconfirm>
                                                )}
                                            </div>
                                        </div>
                                    );
                                })}
                            </Space>
                        )}
                    </Card>
                </Col>
            </Row>

            {/* Create schedule modal */}
            <Modal
                title={`Add Schedule — ${selectedDate.format("DD/MM/YYYY")}`}
                open={createOpen}
                onCancel={() => setCreateOpen(false)}
                onOk={() => form.submit()}
                confirmLoading={createMutation.isPending}
                okText="Create"
            >
                <Form
                    form={form}
                    layout="vertical"
                    onFinish={handleCreate}
                    style={{ marginTop: 16 }}
                >
                    <Form.Item
                        name="doctorId"
                        label="Doctor"
                        rules={[{ required: true, message: "Select a doctor" }]}
                    >
                        <Select
                            placeholder="Select doctor"
                            showSearch
                            filterOption={(input, option) =>
                                (option?.label as string)
                                    ?.toLowerCase()
                                    .includes(input.toLowerCase())
                            }
                            options={doctors.map((d) => ({
                                value: d.id,
                                label: d.fullName,
                            }))}
                        />
                    </Form.Item>

                    <Form.Item
                        name="type"
                        label="Shift"
                        rules={[{ required: true, message: "Select a shift" }]}
                    >
                        <div style={{ display: "flex", gap: 10 }}>
                            {(
                                Object.entries(SHIFT_CONFIG) as [
                                    keyof typeof SHIFT_CONFIG,
                                    (typeof SHIFT_CONFIG)[keyof typeof SHIFT_CONFIG],
                                ][]
                            ).map(([key, cfg]) => (
                                <Form.Item
                                    key={key}
                                    name="type"
                                    noStyle
                                >
                                    <ShiftCard
                                        shiftKey={key}
                                        config={cfg}
                                        selected={form.getFieldValue("type") === key}
                                        onSelect={(k) => form.setFieldValue("type", k)}
                                    />
                                </Form.Item>
                            ))}
                        </div>
                    </Form.Item>

                    <Form.Item
                        name="maxPatients"
                        label="Max Patients"
                        rules={[
                            { required: true, message: "Required" },
                            {
                                type: "number",
                                min: 5,
                                max: 30,
                                message: "Between 5 and 30",
                            },
                        ]}
                        initialValue={20}
                    >
                        <InputNumber
                            min={5}
                            max={30}
                            style={{ width: "100%" }}
                        />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
}

function ShiftCard({
    shiftKey,
    config,
    selected,
    onSelect,
}: {
    shiftKey: string;
    config: { label: string; time: string; color: string; bg: string; borderColor: string };
    selected: boolean;
    onSelect: (key: string) => void;
}) {
    return (
        <div
            onClick={() => onSelect(shiftKey)}
            style={{
                flex: 1,
                padding: "10px 12px",
                borderRadius: 8,
                border: `2px solid ${selected ? config.color : "#d9d9d9"}`,
                background: selected ? config.bg : "#fafafa",
                cursor: "pointer",
                textAlign: "center",
                transition: "all 0.2s",
            }}
        >
            <Text
                strong
                style={{ color: selected ? config.color : "#595959", fontSize: 13 }}
            >
                {config.label}
            </Text>
            <br />
            <Text type="secondary" style={{ fontSize: 11 }}>
                {config.time}
            </Text>
        </div>
    );
}
