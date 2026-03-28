package com.hospital.hms.appointment.service;

import com.hospital.hms.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentSlotService {

    private static final String BOOK_SLOT_SCRIPT = """
            local current = redis.call('GET', KEYS[1])
            
            if not current then
                redis.call('SET', KEYS[1], ARGV[1])
            end
            
            local val = redis.call('INCR', KEYS[1])
            if val > tonumber(ARGV[2]) then
                redis.call('DECR', KEYS[1])
                return 0
            end
            
            return 1
            """;
    private static final String CANCEL_SLOT_SCRIPT = """
            local current = redis.call('GET', KEYS[1])
            
            if not current then
                return 0
            end
            
            local val = redis.call('DECR', KEYS[1])
            if val < 0 then
                redis.call('INCR', KEYS[1])
                return 0
            end
            
            return 1
            """;

    private static final RedisScript<Long> BOOK_SLOT_REDIS_SCRIPT = RedisScript.of(BOOK_SLOT_SCRIPT, Long.class);
    private static final RedisScript<Long> CANCEL_SLOT_REDIS_SCRIPT = RedisScript.of(CANCEL_SLOT_SCRIPT, Long.class);

    private final StringRedisTemplate template;
    private final AppointmentRepository appointmentRepository;

    public boolean bookSlot(UUID doctorScheduleId, String key, Integer maxPatients) {
        Integer count = 0;
        if (template.opsForValue().get(key) == null) {
            count = appointmentRepository.countAppointmentByDoctorSchedule_Id(doctorScheduleId);
        }

        Long value = template.execute(
                BOOK_SLOT_REDIS_SCRIPT,
                List.of(key),
                count.toString(),
                maxPatients.toString()
        );

        return value != null && value > 0;
    }

    public boolean cancelSlot(String key) {
        Long value = template.execute(
                CANCEL_SLOT_REDIS_SCRIPT,
                List.of(key)
        );

        log.debug("Slot cancel result for key {}: {}", key, value);

        return value != null && value > 0;
    }
}
