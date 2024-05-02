package com.example.service.user.model;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String publicId;
    private String username;
}