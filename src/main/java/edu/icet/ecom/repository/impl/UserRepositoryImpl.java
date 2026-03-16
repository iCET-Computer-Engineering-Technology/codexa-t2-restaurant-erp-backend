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

    @Override
    public UserEntity findByUsername(String username) {
        try {
            return template.queryForObject("SELECT * FROM users WHERE username=?", (rs, rowNum) ->
                    new UserEntity(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            Role.valueOf(rs.getString("role")),
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
                            Role.valueOf(rs.getString("role")),
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
                userEntity.getRole().name(),
                userEntity.getEnabled(),
                userEntity.getCreatedAt());
        return findByUsername(userEntity.getUsername());
    }
}
