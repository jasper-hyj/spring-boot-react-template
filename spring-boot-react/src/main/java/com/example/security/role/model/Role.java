package com.example.security.role.model;

import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class Role implements GrantedAuthority {

    private String id;
    private String description;

    /**
     * Method for GrantedAuthority
     * Standard name: "ROLE_{id}"
     * Method Available: hasRole, hasAnyRole
     */
    @Override
    public String getAuthority() {
        return "ROLE_" + id;
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
        Role role = (Role) obj;
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
