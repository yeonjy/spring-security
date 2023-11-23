package com.example.springsecurity;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getString() {
        return role;
    }
}
