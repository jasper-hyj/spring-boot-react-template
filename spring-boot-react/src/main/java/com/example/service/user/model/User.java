package com.example.service.user.model;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.example.model.Model;
import lombok.Data;

@Data
public class User implements Model<User> {

    private UUID id;
    @JsonIgnore
    private String password;

    private String publicId;
    private String email;

    private UserStatus status;

    private String username;

    private String lastname;
    private String firstname;
    private String imageUrl;

    private List<? extends GrantedAuthority> authorities;

    @Override
    public User clone() {
        User user = new User();
        user.setId(this.id);
        user.setPublicId(this.publicId);
        user.setEmail(this.email);
        user.setStatus(this.status);
        user.setUsername(this.username);
        user.setLastname(this.lastname);
        user.setFirstname(this.firstname);
        user.setImageUrl(this.imageUrl);
        return user;
    }
}
