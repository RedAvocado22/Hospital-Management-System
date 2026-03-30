package com.hospital.hms.auth.dto;

import com.hospital.hms.auth.entity.Role;

public record RoleInfo(
        String name
) {
    public static RoleInfo from(Role role) {
        return new RoleInfo(role.getName());
    }
}
