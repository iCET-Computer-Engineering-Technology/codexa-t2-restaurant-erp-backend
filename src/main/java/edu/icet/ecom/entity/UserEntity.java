package edu.icet.ecom.entity;

import edu.icet.ecom.util.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private Boolean enabled;
    private LocalDateTime createdAt;
}
