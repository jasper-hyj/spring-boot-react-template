package com.example.security.role;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.security.role.model.Role;
import com.example.service.user.model.User;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Get all roles for specify user
     *
     * @param user user information
     * @return list of user's roles
     */
    public List<String> getUserRoleIdList(User user) {
        return roleRepository.findAllByUserId(user.getId()).stream()
                .map(Role::getId)
                .collect(Collectors.toList());
    }
}
