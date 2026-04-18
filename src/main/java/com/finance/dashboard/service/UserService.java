package com.finance.dashboard.service;
import com.finance.dashboard.dto.UserRequest;
import com.finance.dashboard.dto.UserResponse;
import com.finance.dashboard.exception.AccessDeniedException;
import com.finance.dashboard.exception.DuplicateResourceException;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.model.Role;
import com.finance.dashboard.model.User;
import com.finance.dashboard.model.UserStatus;
import com.finance.dashboard.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserResponse createUser(UserRequest request, Role requesterRole) {
        if (requesterRole != Role.ADMIN) {
            throw new AccessDeniedException("Only ADMIN users can create new users.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                "A user with email '" + request.getEmail() + "' already exists."
            );
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException(
                "A user with username '" + request.getUsername() + "' already exists."
            );
        }
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword()); 
        newUser.setRole(request.getRole());
        newUser.setStatus(UserStatus.ACTIVE); 
        User savedUser = userRepository.save(newUser);
        return convertToResponse(savedUser);
    }
    public UserResponse getUserById(Long userId) {
        User user = findUserOrThrow(userId);
        return convertToResponse(user);
    }
    public List<UserResponse> getAllUsers(Role requesterRole) {
        if (requesterRole == Role.VIEWER) {
            throw new AccessDeniedException("VIEWER role cannot access the full user list.");
        }
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)  
                .collect(Collectors.toList());
    }
    public UserResponse updateUserRole(Long userId, Role newRole, Role requesterRole) {
        if (requesterRole != Role.ADMIN) {
            throw new AccessDeniedException("Only ADMIN can change user roles.");
        }
        User user = findUserOrThrow(userId);
        user.setRole(newRole);
        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }
    public UserResponse updateUserStatus(Long userId, UserStatus newStatus, Role requesterRole) {
        if (requesterRole != Role.ADMIN) {
            throw new AccessDeniedException("Only ADMIN can change user status.");
        }
        User user = findUserOrThrow(userId);
        user.setStatus(newStatus);
        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }
    public void deleteUser(Long userId, Role requesterRole) {
        if (requesterRole != Role.ADMIN) {
            throw new AccessDeniedException("Only ADMIN can delete users.");
        }
        User user = findUserOrThrow(userId); 
        userRepository.delete(user);
    }
    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "User with ID " + userId + " not found."
                ));
    }
    public UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
