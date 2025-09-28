package com.InFrame.domains.user.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import com.InFrame.domains.user.entity.User;
import com.InFrame.domains.user.repository.UserRepository;
import com.InFrame.domains.user.reqdto.SignInRequestDto;
import com.InFrame.domains.user.reqdto.SignUpRequestDto;
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

    // 회원가입
    @Transactional
    public AuthResponseDto signup(SignUpRequestDto signupRequestDto) {
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

    // 로그인
    @Transactional
    public AuthResponseDto signin(SignInRequestDto signInRequestDto) {
        User user = userRepository.findByEmail(signInRequestDto.email())
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(signInRequestDto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        String accessToken = jwtUtil.createAccessToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        return AuthResponseDto.from(user, accessToken);
    }
}