package com.example.service.user;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.exception.request.NotFoundException;
import com.example.exception.request.StaleUpdateException;
import com.example.exception.request.UpdateRejectedException;
import com.example.model.ApiResponse;
import com.example.security.UserPrincipal;
import com.example.service.user.model.User;
import com.example.service.user.model.UserUpdateRequest;

@Controller
@RequestMapping("/auth/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get current user info
     *
     * @return user info
     */
    @GetMapping("/current")
    public ResponseEntity<ApiResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, "Current user info retrieved successfully",
                        userPrincipal.user()));
    }

    /**
     * Get all user information
     *
     * @return all user info
     * @authorize ROLE_admin
     */
    @GetMapping({ "", "/{id}" })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getUser(@PathVariable(name = "id", required = false) UUID id) {
        try {
            Object userData;
            if (id != null) {
                userData = userService.getUserById(id);
            } else {
                userData = userService.getAllUserList();
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success(HttpStatus.OK, "User info retrieved successfully", userData));
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    /**
     * Update current user info
     *
     * @param userUpdateRequest user update request information
     * @return user info with updated information
     * @throws NotFoundException
     * @throws UpdateRejectedException
     */
    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
            @AuthenticationPrincipal UserPrincipal principal) throws NotFoundException, UpdateRejectedException {
        try {
            User user = userService.updateUser(principal.user(), userUpdateRequest);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success(HttpStatus.OK, "User info updated successfully", user));
        } catch (StaleUpdateException e) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.error(HttpStatus.NO_CONTENT, e.getMessage()));
        }
    }
}
