package com.hospital.hms.auth.service;

import com.hospital.hms.auth.entity.Role;
import com.hospital.hms.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleQueryService {
    private final RoleRepository roleRepository;

    public UUID getRoleIdByName(String name) {
        return roleRepository.findByNameIgnoreCase(name).orElseThrow(
                () -> new RuntimeException("Role not found")
        ).getId();
    }

    public Role getReferenceById(UUID id) {
        return roleRepository.getReferenceById(id);
    }
}
