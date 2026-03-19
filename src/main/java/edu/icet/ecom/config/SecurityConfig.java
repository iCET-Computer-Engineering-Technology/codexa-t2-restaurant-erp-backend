package edu.icet.ecom.config;

import edu.icet.ecom.filter.JwtAuthFilter;
import edu.icet.ecom.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService service;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http.csrf(AbstractHttpConfigurer::disable)
                    .cors(Customizer.withDefaults())
                    .sessionManagement(sessionConfig ->
                            sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(authConfig -> authConfig
                            // Public endpoints
                            .requestMatchers("/api/auth/login").permitAll()
                            .requestMatchers("/api/auth/register").permitAll()
                            .requestMatchers("/v3/api-docs/**").permitAll()
                            .requestMatchers("/swagger-ui/**").permitAll()
                            .requestMatchers("/swagger-ui.html").permitAll()
                            .requestMatchers("/order/**").permitAll()
                            .requestMatchers("/customers/**").permitAll()
                            .requestMatchers("/api/kitchen/**").permitAll()
                            .requestMatchers("/category/**").permitAll()
                            .requestMatchers("/item/**").permitAll()
                            .requestMatchers("/modifier-group/**").permitAll()
                            .requestMatchers("/modifiers/**").permitAll()
                            .requestMatchers("/menu-item-modifier-group/**").permitAll()

                            // Admin endpoints - MUST be before other patterns
                            .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                            .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                            .requestMatchers("/ingredient/**").hasAuthority("ROLE_ADMIN")

                            // Marketing/Campaign Analytics endpoints - Admin only
                            .requestMatchers("/api/campaigns/**").hasAuthority("ROLE_ADMIN")
                            .requestMatchers("/api/campaign-analytics/**").hasAuthority("ROLE_ADMIN")

                            // User endpoints
                            .requestMatchers("/user/**").hasAuthority("ROLE_USER")

                            // All other requests require authentication
                            .anyRequest().authenticated()
                    )
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        } catch (Exception e) {
            throw new BeanCreationException("securityFilterChain", "Failed to configure security filter chain", e);
        }
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173", "http://localhost:4200")); // Add your frontend URLs here
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(service);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        try {
            return config.getAuthenticationManager();
        } catch (Exception e) {
            throw new BeanCreationException("authenticationManager", "Failed to get AuthenticationManager", e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}