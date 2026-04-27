package com.hexaquiz.enums;

public enum UserTypeEnum {
    USER("USER"),
    ADM("ADM");

    private final String value;
    UserTypeEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
