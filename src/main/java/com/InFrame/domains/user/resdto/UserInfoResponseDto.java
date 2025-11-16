package com.InFrame.domains.user.resdto;

import com.InFrame.domains.user.entity.Role;
import com.InFrame.domains.user.entity.User;

public record UserInfoResponseDto(
        Long id,
        String email,
        String nickname,
        String name,
        String profileImageUrl,
        Role role,
        long reservationCount,
        long hostLikeCount,
        long experienceLikeCount,
        long reviewCount
) {
    public static UserInfoResponseDto from(User user, long reservationCount, long hostLikeCount, long experienceLikeCount, long reviewCount) {
        return new UserInfoResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getName(),
                user.getProfileImageUrl(),
                user.getRole(),
                reservationCount,
                hostLikeCount,
                experienceLikeCount,
                reviewCount

        );
    }
}
