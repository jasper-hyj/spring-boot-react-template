package com.example.service.user;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.example.exception.request.NotFoundException;
import com.example.exception.request.StaleUpdateException;
import com.example.exception.request.UpdateRejectedException;
import com.example.repository.sql.Operation;
import com.example.security.authority.AuthorityRepository;
import com.example.security.authority.model.Authority;
import com.example.security.role.RoleRepository;
import com.example.security.role.model.Role;
import com.example.service.user.model.User;
import com.example.service.user.model.UserRegisterRequest;
import com.example.service.user.model.UserStatus;
import com.example.service.user.model.UserUpdateRequest;
import com.example.util.ObjectUtil;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
            AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    public List<User> getAllUserList() {
        return userRepository.findAll().stream()
                .peek(user -> {
                    setUpUser(user);
                })
                .collect(Collectors.toList());
    }

    public User getUserById(UUID id) throws NotFoundException {
        User user = userRepository.findById(id);
        setUpUser(user);
        return user;
    }

    public User getUserByEmail(String email) throws NotFoundException {
        User user = userRepository.findByEmail(email);
        setUpUser(user);
        return user;
    }

    public User updateUser(User user, UserUpdateRequest userUpdateRequest)
            throws NotFoundException, StaleUpdateException, UpdateRejectedException {
        User updatedUser = ObjectUtil.update(user.clone(), userUpdateRequest);
        updatedUser.setPublicId(updatedUser.getPublicId().trim());

        if (updatedUser.getPublicId().contains(" ")) {
            updatedUser.setPublicId(user.getPublicId().replaceAll("\\s+", "-"));
        }
        if (!isValidPublicId(updatedUser)) {
            throw new UpdateRejectedException("Public Id exists");
        }
        // Update user using userUpdateRequest, Save updated user to database
        userRepository.save(user.getId(), user, updatedUser, Operation.UPDATE);

        // Get Updated Information
        return getUserById(user.getId());
    }

    public User createUser(UserRegisterRequest userRegisterRequest) throws StaleUpdateException, NotFoundException {
        UUID id = UUID.randomUUID();
        // Save user to the repository
        User user = new User();
        User updatedUser = ObjectUtil.update(user, userRegisterRequest);
        updatedUser.setId(id);
        updatedUser
                .setPublicId(generatePublicId(userRegisterRequest.getFirstname(), userRegisterRequest.getLastname()));
        updatedUser.setStatus(UserStatus.active);

        userRepository.save(id, null, updatedUser, Operation.INSERT);

        // Fetch the newly created user from the database
        return getUserByEmail(userRegisterRequest.getEmail());
    }

    private String generatePublicId(String firstname, String lastname) {
        UUID id = UUID.randomUUID();
        String uuidString = id.toString().replace("-", "");
        int randomIndex = (int) (Math.random() * (uuidString.length() - 5));
        String randomLetters = uuidString.substring(randomIndex, randomIndex + 5);
        return String.format("%s-%s-%s", firstname.toLowerCase(), lastname.toLowerCase(), randomLetters);
    }

    private void setUpUser(User user) {
        List<Role> roles = roleRepository.findAllByUserId(user.getId());
        List<Authority> authorities = authorityRepository.findAllByUserId(user.getId());
        user.setAuthorities(Stream.concat(roles.stream(), authorities.stream()).collect(Collectors.toList()));
    }

    private boolean isValidPublicId(User user) {
        try {
            User userFound = userRepository.findByPublicId(user.getPublicId());
            return userFound.getId() == user.getId();
        } catch (NotFoundException e) {
            return true;
        }
    }
}
