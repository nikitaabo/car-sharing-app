package com.example.carsharingapp.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    MANAGER,
    CUSTOMER;

    @Override
    public String getAuthority() {
        return name();
    }
}
