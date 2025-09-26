package com.InFrame.domains.user.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import com.InFrame.domains.user.entity.User;
import com.InFrame.domains.user.repository.UserRepository;
import com.InFrame.domains.user.reqdto.SignupRequestDto;
import com.InFrame.domains.user.resdto.AuthResponseDto;
import com.InFrame.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponseDto signup(SignupRequestDto signupRequestDto) { // 회원가입
        if (userRepository.findByEmail(signupRequestDto.email()).isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXIST);
        }
        if (userRepository.findByEmail(signupRequestDto.nickname()).isPresent()) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXIST);
        }
        String encodedPassword = passwordEncoder.encode(signupRequestDto.password());

        User savedUser = userRepository.save(signupRequestDto.toEntity(encodedPassword));

        String accessToken = jwtUtil.createAccessToken(
                    savedUser.getId(),
                    savedUser.getEmail(),
                    savedUser.getRole().name()
        );
        return AuthResponseDto.from(savedUser, accessToken);
    }
}