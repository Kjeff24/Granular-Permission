package com.bexos.granular_permission.controllers;

import com.bexos.granular_permission.dto.AuthRequest;
import com.bexos.granular_permission.dto.AuthenticationResponse;
import com.bexos.granular_permission.dto.RegisterRequest;
import com.bexos.granular_permission.services.interfaces.UserService;
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
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(userService.authenticate(request));
    }
}
