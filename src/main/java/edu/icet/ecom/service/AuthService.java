package edu.icet.ecom.service;

import edu.icet.ecom.dto.AuthResponse;
import edu.icet.ecom.dto.LoginRequestDto;
import edu.icet.ecom.dto.RegisterRequestDto;
import edu.icet.ecom.util.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface AuthService {
    AuthResponse register(RegisterRequestDto request);
    AuthResponse login(LoginRequestDto request);
    void logout(String username);
    void heartbeat(String username);
    Role getRoleFromAuthorities(Collection<? extends GrantedAuthority> authorities);
}
