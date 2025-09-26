package com.InFrame.domains.user.resdto;

import com.InFrame.domains.user.entity.Role;
import com.InFrame.domains.user.entity.User;

public record AuthResponseDto(
        Long id,
        String email,
        String nickname,
        String name,
        Role role,
        String accessToken
) {
    public static AuthResponseDto from(User user, String accessToken) {
        return new AuthResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getName(),
                user.getRole(),
                accessToken
        );
    }
}