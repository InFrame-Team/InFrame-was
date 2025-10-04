package com.InFrame.domains.user.resdto;

import com.InFrame.domains.user.entity.Role;
import com.InFrame.domains.user.entity.User;

public record UserInfoResponseDto(
        Long id,
        String email,
        String nickname,
        String name,
        Role role
) {
    public static UserInfoResponseDto from(User user) {
        return new UserInfoResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getName(),
                user.getRole()
        );
    }
}
