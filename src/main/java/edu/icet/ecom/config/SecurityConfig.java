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
import org.springframework.http.HttpMethod;
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

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/register",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/error",
            "/ws/**"
    };

    private static final String[] ADMIN_ENDPOINTS = {
            "/api/admin/**",
            "/admin/**",
            "/ingredient/**",
            "/api/campaigns/**",
            "/api/campaign-analytics/**",
            "/api/emails/**",
            "/api/categories/**",
            "/api/menu-items/**",
            "/api/portions/**",
            "/api/portions/**",
            "/api/menu-item-price/**",
            "/api/reconciliation/**",
            "/api/revenue/**",
            "/api/floor-sections/**"

    };
    private static final String[] MANAGER_ENDPOINTS = {
            "/allowance/**",
            "/besic-salary/**",
            "/deduction/**",
            "/employee/**",
            "/employee-leave/**",
            "/overtime/**",
            "/payroll-config/**",
            "/payroll/**",
            "/salary-request/**",
            "/salary-response/**",
            "/api/supplier/**",
            "/bonus/**"
    };
    private static final String[] STAFF_ENDPOINTS = {
            "/api/order/**",
            "/api/kitchen/**",
            "/customers/**",
            "/api/waiter/**",
            "/api/payments/**",
            "/api/supplier/**",
            "/api/auth/logout",
            "/api/auth/heartbeat"
    };

    // Staff screens need read access to menu master data, while writes remain admin-only.
    private static final String[] STAFF_READONLY_ENDPOINTS = {
            "/api/categories/**",
            "/api/menu-items/**",
            "/api/menu-item-price/**",
            "/api/portions/**",
            "/api/portions/**",
            "/tables/**",
            "/api/tables/**"

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http.csrf(AbstractHttpConfigurer::disable)
                    .cors(Customizer.withDefaults())
                    .sessionManagement(sessionConfig ->
                            sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(authConfig -> {
                        authConfig.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                        authConfig.requestMatchers(PUBLIC_ENDPOINTS).permitAll();
                        authConfig.requestMatchers(HttpMethod.GET, STAFF_READONLY_ENDPOINTS)
                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_CASHIER", "ROLE_WAITER", "ROLE_CHEF","ROLE_MANAGER");
                        authConfig.requestMatchers(ADMIN_ENDPOINTS).hasAuthority("ROLE_ADMIN");
                        authConfig.requestMatchers(MANAGER_ENDPOINTS).hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN");
                        authConfig.requestMatchers(STAFF_ENDPOINTS)
                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_CASHIER", "ROLE_WAITER", "ROLE_CHEF");
                        authConfig.requestMatchers("/user/**").hasAuthority("ROLE_USER");
                        authConfig.anyRequest().authenticated();
                    })
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
        // Use allowedOriginPatterns instead of allowedOrigins when allowCredentials is true
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
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
