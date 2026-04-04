package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.UserEntity;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate template;

    private Role parseRole(String roleValue) {
        if (roleValue == null || roleValue.isBlank()) {
            return null;
        }
        return Role.valueOf(roleValue);
    }

    @Override
    public UserEntity findByUsername(String username) {
        try {
            return template.queryForObject("SELECT * FROM users WHERE username=?", (rs, rowNum) ->
                    new UserEntity(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            parseRole(rs.getString("role")),
                            rs.getBoolean("enabled"),
                            rs.getBoolean("is_online"),
                            rs.getTimestamp("last_active_at") != null ? rs.getTimestamp("last_active_at").toLocalDateTime() : null,
                            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null
                    ), username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public UserEntity findByEmail(String email) {
        try {
            return template.queryForObject("SELECT * FROM users WHERE email=?", (rs, rowNum) ->
                    new UserEntity(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            parseRole(rs.getString("role")),
                            rs.getBoolean("enabled"),
                            rs.getBoolean("is_online"),
                            rs.getTimestamp("last_active_at") != null ? rs.getTimestamp("last_active_at").toLocalDateTime() : null,
                            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null
                    ), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        template.update("INSERT INTO users (username, email, password, role, enabled, is_online, created_at) VALUES (?,?,?,?,?,?,?)",
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRole() != null ? userEntity.getRole().name() : null,
                userEntity.getEnabled(),
                userEntity.getIsOnline() != null ? userEntity.getIsOnline() : false, 
                userEntity.getCreatedAt());
        return findByUsername(userEntity.getUsername());
    }

    @Override
    public void updateIsOnline(Long userId, boolean isOnline) {
        template.update("UPDATE users SET is_online = ? WHERE id = ?", isOnline, userId);
    }

    @Override
    public void updateLastActiveAt(Long userId, java.time.LocalDateTime lastActiveAt) {
        template.update("UPDATE users SET last_active_at = ? WHERE id = ?", lastActiveAt, userId);
    }

    @Override
    public void markOfflineIfInactive(java.time.LocalDateTime threshold) {
        template.update("UPDATE users SET is_online = false WHERE is_online = true AND last_active_at < ?", threshold);
    }

    @Override
    public java.util.List<UserEntity> findByRole(String role) {
        String sql = "SELECT * FROM users WHERE role = ?";
        return template.query(sql, (rs, rowNum) -> 
                new UserEntity(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        parseRole(rs.getString("role")),
                        rs.getBoolean("enabled"),
                        rs.getBoolean("is_online"),
                        rs.getTimestamp("last_active_at") != null ? rs.getTimestamp("last_active_at").toLocalDateTime() : null,
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null
                ), role);
    }
}
