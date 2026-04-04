package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.AuthResponse;
import edu.icet.ecom.dto.LoginRequestDto;
import edu.icet.ecom.dto.RegisterRequestDto;
import edu.icet.ecom.entity.UserEntity;
import edu.icet.ecom.exception.AuthenticationException;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.AuthService;
import edu.icet.ecom.service.CustomUserDetailsService;
import edu.icet.ecom.service.JwtService;
import edu.icet.ecom.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public AuthResponse register(RegisterRequestDto request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new IllegalArgumentException("Username already taken");
        if (userRepository.existsByEmail(request.getEmail()))
            throw new IllegalArgumentException("Email already registered");
        Role role;
        if (request.getRole() != null) {
            role = request.getRole();
        } else {
            role = Role.ROLE_USER;
        }
        UserEntity entity = new UserEntity();
        entity.setUsername(request.getUsername());
        entity.setEmail(request.getEmail());
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setRole(role);
        entity.setEnabled(true);
        entity.setCreatedAt(LocalDateTime.now());
        UserEntity savedEntity = userRepository.save(entity);

        UserDetails details = userDetailsService.loadUserByUsername(savedEntity.getUsername());
        String token = jwtService.generateToken(details);
        return new AuthResponse(token, savedEntity.getUsername(), role, savedEntity.getId());
    }

    @Override
    public AuthResponse login(LoginRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new AuthenticationException("Username or password is incorrect");
        }
        
        UserEntity userEntity = userRepository.findByUsername(request.getUsername());
        Long userId = null;
        if (userEntity != null) {
            userRepository.updateIsOnline(userEntity.getId(), true);
            userRepository.updateLastActiveAt(userEntity.getId(), java.time.LocalDateTime.now());
            userId = userEntity.getId();
        }

        UserDetails details = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(details);
        Role role = getRoleFromAuthorities(details.getAuthorities());
        return new AuthResponse(token, request.getUsername(), role, userId);
    }

    @Override
    public void logout(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            userRepository.updateIsOnline(userEntity.getId(), false);
        }
    }

    @Override
    public void heartbeat(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            userRepository.updateLastActiveAt(userEntity.getId(), java.time.LocalDateTime.now());
            if (!Boolean.TRUE.equals(userEntity.getIsOnline())) {
                userRepository.updateIsOnline(userEntity.getId(), true);
            }
        }
    }

    @org.springframework.scheduling.annotation.Scheduled(fixedRate = 60000)
    public void markInactiveUsersOffline() {
        // Mark users offline if they haven't sent a heartbeat in the last 2 minutes
        java.time.LocalDateTime threshold = java.time.LocalDateTime.now().minusMinutes(2);
        userRepository.markOfflineIfInactive(threshold);
    }

    @Override
    public Role getRoleFromAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .findFirst()
                .map(a -> Role.valueOf(a.getAuthority()))
                .orElse(Role.ROLE_USER);
    }
}
