package com.example.service.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    private String email;
    private String firstname;
    private String lastname;
    private String username;
    private String imageUrl;
    private String password;
}
