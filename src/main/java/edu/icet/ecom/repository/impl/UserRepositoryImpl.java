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
        template.update("INSERT INTO users (username, email, password, role, enabled, created_at) VALUES (?,?,?,?,?,?)",
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRole() != null ? userEntity.getRole().name() : null,
                userEntity.getEnabled(),
                userEntity.getCreatedAt());
        return findByUsername(userEntity.getUsername());
    }
}
