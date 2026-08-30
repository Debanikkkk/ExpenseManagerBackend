package com.example.demo.entity.task;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatus {
    TODO("Todo"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private final String label;

    TaskStatus(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static TaskStatus fromValue(String value) {
        for (TaskStatus status : values()) {
            if (status.label.equalsIgnoreCase(value) || status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown TaskStatus: " + value);
    }
}