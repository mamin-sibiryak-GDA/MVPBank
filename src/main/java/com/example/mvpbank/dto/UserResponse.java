package com.example.mvpbank.dto;

import com.example.mvpbank.model.Role;

public record UserResponse(Long id, String username, String password, String email, Role role) {
}
