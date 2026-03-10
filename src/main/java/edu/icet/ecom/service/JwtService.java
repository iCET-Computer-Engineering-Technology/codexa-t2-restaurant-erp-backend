package edu.icet.ecom.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    String getFirstAuthority(Collection<? extends GrantedAuthority> authorities);
    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);
    String extractUsername(String token);
    String extractRole(String token);
    Date extractExpiration(String token);
    Claims extractAllClaims(String token);
    SecretKey getSigningKey();
}
