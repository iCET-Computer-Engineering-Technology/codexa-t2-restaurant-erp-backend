package edu.icet.ecom.entity;

import edu.icet.ecom.util.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private Boolean enabled;
    private Boolean isOnline;
    private LocalDateTime lastActiveAt;
    private LocalDateTime createdAt;
    
    // Add compatible constructor for usages that didn't know about isOnline
    public UserEntity(Long id, String username, String email, String password, Role role, Boolean enabled, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.isOnline = false;
        this.lastActiveAt = LocalDateTime.now();
        this.createdAt = createdAt;
    }

    public UserEntity(Long id, String username, String email, String password, Role role, Boolean enabled, Boolean isOnline, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.isOnline = isOnline;
        this.lastActiveAt = LocalDateTime.now();
        this.createdAt = createdAt;
    }

    public UserEntity(Long id, String username, String email, String password, Role role, Boolean enabled, Boolean isOnline, LocalDateTime lastActiveAt, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.isOnline = isOnline;
        this.lastActiveAt = lastActiveAt;
        this.createdAt = createdAt;
    }
}
