package com.dreamx.tasks.model;

import java.time.LocalDateTime;

public record User(
        Long id,
        String firstname,
        String lastName,
        String email,
        String phone,
        String password,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}

