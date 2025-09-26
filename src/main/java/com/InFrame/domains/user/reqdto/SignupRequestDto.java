package com.InFrame.domains.user.reqdto;


import com.InFrame.domains.user.entity.Role;
import com.InFrame.domains.user.entity.User;

public record SignupRequestDto(
        String email,
        String password,
        String nickname,
        String name,
        Role role
) {
    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .nickname(nickname)
                .name(name)
                .role(role)
                .build();
    }
}
