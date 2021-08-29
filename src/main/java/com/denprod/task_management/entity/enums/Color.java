package com.denprod.task_management.entity.enums;

public enum Color {
    PRIMARY,
    SECONDARY,
    SUCCESS,
    DANGER,
    WARNING,
    INFO,
    //LIGHT,
    DARK;

    public String toLowerCase() {
        return toString().toLowerCase();
    }
}
