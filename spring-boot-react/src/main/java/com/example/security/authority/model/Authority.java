package com.example.security.authority.model;

import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class Authority implements GrantedAuthority {

    private String id;
    private String description;

    @Override
    public String getAuthority() {
        return id;
    }

    /**
     * Custom equals
     * Check id for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Authority role = (Authority) obj;
        return Objects.equals(id, role.id);
    }

    /**
     * Custom hashcode by id
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
