package edu.icet.ecom.controller;

import edu.icet.ecom.dto.AuthResponse;
import edu.icet.ecom.dto.LoginRequestDto;
import edu.icet.ecom.dto.RegisterRequestDto;
import edu.icet.ecom.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequestDto request){
        return ResponseEntity.status(201).body(service.register(request));
    }

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequestDto request){
        return ResponseEntity.status(200).body(service.login(request));
    }
}
