package com.hospital.hms.common.enums;

public enum AuditAction {
    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete");
    private final String description;

    AuditAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
