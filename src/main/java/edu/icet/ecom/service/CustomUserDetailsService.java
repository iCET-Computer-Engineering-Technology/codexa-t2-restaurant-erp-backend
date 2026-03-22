package edu.icet.ecom.service;

import edu.icet.ecom.entity.UserEntity;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.util.Role;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = repository.findByUsername(username);
        if (entity == null) throw new UsernameNotFoundException("User not found: "+username);
        Role effectiveRole = entity.getRole() != null ? entity.getRole() : Role.ROLE_USER;
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(effectiveRole.name());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return new User(
                entity.getUsername(),
                entity.getPassword(),
                entity.getEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}
