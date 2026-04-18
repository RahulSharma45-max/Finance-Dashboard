package com.finance.dashboard.dto;
import com.finance.dashboard.model.Role;
import com.finance.dashboard.model.UserStatus;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
