package com.stinkytooters.stinkytootersbot.api.internal.user;

import java.util.stream.Stream;

public enum UserStatus {
    ACTIVE("A"),
    INACTIVE("I");

    private final String code;

    UserStatus(String code)  {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static UserStatus fromValue(String value) {
        return Stream.of(values()).filter(v -> v.name().equalsIgnoreCase(value) || v.getCode().equalsIgnoreCase(value)).findFirst().orElse(null);
    }
}
