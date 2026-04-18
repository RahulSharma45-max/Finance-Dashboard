package com.finance.dashboard.controller;
import com.finance.dashboard.dto.UserRequest;
import com.finance.dashboard.dto.UserResponse;
import com.finance.dashboard.model.Role;
import com.finance.dashboard.model.UserStatus;
import com.finance.dashboard.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest request,
            @RequestHeader("X-User-Role") Role requesterRole) {
        UserResponse created = userService.createUser(request, requesterRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id,
            @RequestHeader("X-User-Role") Role requesterRole) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @RequestHeader("X-User-Role") Role requesterRole) {
        List<UserResponse> users = userService.getAllUsers(requesterRole);
        return ResponseEntity.ok(users);
    }
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable Long id,
            @RequestParam Role newRole,
            @RequestHeader("X-User-Role") Role requesterRole) {
        UserResponse updated = userService.updateUserRole(id, newRole, requesterRole);
        return ResponseEntity.ok(updated);
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponse> updateUserStatus(
            @PathVariable Long id,
            @RequestParam UserStatus newStatus,
            @RequestHeader("X-User-Role") Role requesterRole) {
        UserResponse updated = userService.updateUserStatus(id, newStatus, requesterRole);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @RequestHeader("X-User-Role") Role requesterRole) {
        userService.deleteUser(id, requesterRole);
        return ResponseEntity.noContent().build();
    }
}
