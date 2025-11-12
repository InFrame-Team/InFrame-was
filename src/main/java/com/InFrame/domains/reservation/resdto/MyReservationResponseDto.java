package com.InFrame.domains.reservation.resdto;

import com.InFrame.domains.reservation.entity.Reservation;
import com.InFrame.domains.reservation.entity.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "내 예약 내역 응답 DTO")
public record MyReservationResponseDto(
        @Schema(description = "예약 ID")
        Long reservationId,

        @Schema(description = "체험 ID")
        Long experienceId,

        @Schema(description = "예약 상태")
        ReservationStatus status,

        @Schema(description = "호스트 이름")
        String hostName,

        @Schema(description = "체험 제목")
        String experienceTitle,

        @Schema(description = "예약된 시작 시간")
        LocalDateTime reservedStartTime,

        @Schema(description = "총 가격")
        int totalPrice,

        @Schema(description = "리뷰 작성 여부")
        boolean reviewWritten
) {
    public static MyReservationResponseDto from(Reservation reservation, boolean reviewWritten) {
        return new MyReservationResponseDto(
                reservation.getId(),
                reservation.getExperience().getId(),
                reservation.getStatus(),
                reservation.getExperience().getHost().getUser().getName(),
                reservation.getExperience().getTitle(),
                reservation.getReservedStartTime(),
                reservation.getTotalPrice(),
                reviewWritten
        );
    }
}