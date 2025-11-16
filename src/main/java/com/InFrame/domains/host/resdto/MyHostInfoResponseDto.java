package com.InFrame.domains.host.resdto;

import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "내 호스트 정보 응답 DTO")
public record MyHostInfoResponseDto(
        @Schema(description = "호스트 ID")
        Long hostId,

        @Schema(description = "호스트 본명")
        String hostName,

        @Schema(description = "호스트 프로필 이미지")
        String profileImageUrl,

        @Schema(description = "호스트 소개")
        String description,

        @Schema(description = "받은 좋아요 수")
        long hostLikeCount,

        @Schema(description = "받은 리뷰 수")
        long reviewCount,

        @Schema(description = "예약 확정 건수 (예약 완료)")
        long confirmedReservationCount,

        @Schema(description = "체험 완료 건수")
        long completedReservationCount
) {
    public static MyHostInfoResponseDto from(Host host, User user, long likeCount, long reviewCount, long confirmedCount, long completedCount) {
        return new MyHostInfoResponseDto(
                host.getId(),
                user.getName(),
                user.getProfileImageUrl(),
                host.getDescription(),
                likeCount,
                reviewCount,
                confirmedCount,
                completedCount
        );
    }
}