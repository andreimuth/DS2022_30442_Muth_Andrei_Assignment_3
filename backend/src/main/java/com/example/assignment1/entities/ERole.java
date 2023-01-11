package com.example.assignment1.entities;

public enum ERole {
    USER("USER"),
    ADMIN("ADMIN");

    private final String name;

    ERole(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
