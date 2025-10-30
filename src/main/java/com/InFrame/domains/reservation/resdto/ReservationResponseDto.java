package com.InFrame.domains.reservation.resdto;

import com.InFrame.domains.reservation.entity.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "예약 정보 응답 DTO")
public record ReservationResponseDto(
        @Schema(description = "예약 ID")
        Long reservationId,

        @Schema(description = "체험 ID")
        Long experienceId,

        @Schema(description = "사용자 ID")
        Long userId,

        @Schema(description = "예약된 시작 시간 (날짜 포함)")
        LocalDateTime reservedStartTime,

        @Schema(description = "계산된 종료 시간 (날짜 포함)")
        LocalDateTime calculatedEndTime,

        @Schema(description = "예약 성인 인원")
        int numAdults,

        @Schema(description = "예약 아동 인원")
        int numChildren,

        @Schema(description = "총 예약 인원")
        int totalParticipants,

        @Schema(description = "총 가격")
        int totalPrice,

        @Schema(description = "예약 생성 시각")
        LocalDateTime createdAt
) {
    public static ReservationResponseDto from(Reservation reservation) {
        LocalDateTime endTime = reservation.getReservedStartTime()
                .plusHours(reservation.getExperience().getDurationInHours());

        int TotalParticipants = reservation.getNumAdults() + reservation.getNumChildren();

        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getExperience().getId(),
                reservation.getUser().getId(),
                reservation.getReservedStartTime(),
                endTime,
                reservation.getNumAdults(),
                reservation.getNumChildren(),
                TotalParticipants,
                reservation.getTotalPrice(),
                reservation.getCreatedAt()
        );
    }
}
