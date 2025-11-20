package com.InFrame.domains.reservation.resdto;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.reservation.entity.Reservation;
import com.InFrame.domains.reservation.entity.enums.ReservationStatus;
import com.InFrame.domains.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "내 예약 내역 응답 DTO")
public record MyReservationResponseDto(
        @Schema(description = "예약 ID")
        Long reservationId,

        @Schema(description = "체험 ID")
        Long experienceId,

        @Schema(description = "호스트 ID")
        Long hostId,

        @Schema(description = "예약 상태")
        ReservationStatus status,

        @Schema(description = "호스트 이름")
        String hostName,

        @Schema(description = "호스트 프로필 이미지")
        String hostProfileImageUrl,

        @Schema(description = "체험 제목")
        String experienceTitle,

        @Schema(description = "체험 대표 이미지(썸네일)")
        String experienceThumbnailUrl,

        @Schema(description = "체험 유의사항")
        String caution,

        @Schema(description = "예약된 시작 시간")
        LocalDateTime reservedStartTime,

        @Schema(description = "총 가격")
        int totalPrice,

        @Schema(description = "총 예약 인원")
        int totalParticipants,

        @Schema(description = "예약 생성 시간")
        LocalDateTime createdAt,

        @Schema(description = "리뷰 작성 여부")
        boolean reviewWritten,

        @Schema(description = "호스트 좋아요 상태")
        boolean isHostLiked
) {
    public static MyReservationResponseDto from(Reservation reservation, boolean reviewWritten, boolean isHostLiked) {
        Experience experience = reservation.getExperience();
        Host host = experience.getHost();
        User hostUser = host.getUser();

        String imageUrl = null;
        if (experience.getImageUrls() != null && !experience.getImageUrls().isEmpty()) {
            imageUrl = experience.getImageUrls().stream().findFirst().orElse(null);
        }

        int TotalParticipants = reservation.getNumAdults() + reservation.getNumChildren();

        return new MyReservationResponseDto(
                reservation.getId(),
                experience.getId(),
                host.getId(),
                reservation.getStatus(),
                hostUser.getName(),
                hostUser.getProfileImageUrl(),
                experience.getTitle(),
                imageUrl,
                experience.getCaution(),
                reservation.getReservedStartTime(),
                reservation.getTotalPrice(),
                TotalParticipants,
                reservation.getCreatedAt(),
                reviewWritten,
                isHostLiked
        );
    }
}