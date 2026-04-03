package edu.icet.ecom.repository;

import edu.icet.ecom.entity.UserEntity;

public interface UserRepository {
    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    UserEntity save(UserEntity userEntity);
    java.util.List<UserEntity> findByRole(String role);
    void updateIsOnline(Long userId, boolean isOnline);
    void updateLastActiveAt(Long userId, java.time.LocalDateTime lastActiveAt);
    void markOfflineIfInactive(java.time.LocalDateTime threshold);
}
