package com.InFrame.domains.user.controller;

import com.InFrame.domains.user.reqdto.SignInRequestDto;
import com.InFrame.domains.user.reqdto.SignUpRequestDto;
import com.InFrame.domains.user.resdto.AuthResponseDto;
import com.InFrame.domains.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDto signupRequestDto) {
        AuthResponseDto authResponseDto = authService.signup(signupRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDto);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequestDto signInRequestDto) {
        AuthResponseDto authResponseDto = authService.signin(signInRequestDto);

        return ResponseEntity.ok(authResponseDto);
    }
}
