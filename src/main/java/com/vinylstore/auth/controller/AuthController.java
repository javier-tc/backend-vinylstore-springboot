package com.vinylstore.auth.controller;

import com.vinylstore.auth.dto.*;
import com.vinylstore.auth.service.AuthService;
import com.vinylstore.config.RequireAdmin;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        //en una implementación real, invalidarías el token
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable Long userId) {
        UserProfileResponse response = authService.getProfile(userId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserProfileResponse response = authService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/users")
    @RequireAdmin
    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
        List<UserProfileResponse> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/users/{userId}/role")
    @RequireAdmin
    public ResponseEntity<UserProfileResponse> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateRoleRequest request) {
        UserProfileResponse response = authService.updateUserRole(userId, request);
        return ResponseEntity.ok(response);
    }
}

