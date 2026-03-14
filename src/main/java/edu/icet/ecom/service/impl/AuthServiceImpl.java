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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        userRepository.save(entity);

        UserDetails details = userDetailsService.loadUserByUsername(entity.getUsername());
        String token = jwtService.generateToken(details);
        return new AuthResponse(token, entity.getUsername(), role);
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
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Username or password is wrong");
        }
        UserDetails details = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(details);
        Role role = getRoleFromAuthorities(details.getAuthorities());
        return new AuthResponse(token, request.getUsername(), role);
    }

    @Override
    public Role getRoleFromAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .findFirst()
                .map(a -> Role.valueOf(a.getAuthority()))
                .orElse(Role.ROLE_USER);
    }
}
