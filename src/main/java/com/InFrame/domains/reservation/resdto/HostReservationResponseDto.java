package com.InFrame.domains.reservation.resdto;

import com.InFrame.domains.reservation.entity.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Schema(description = "호스트용 예약 목록 응답 DTO")
public record HostReservationResponseDto(
        @Schema(description = "예약 ID")
        Long reservationId,

        @Schema(description = "체험 ID")
        Long experienceId,

        @Schema(description = "체험 제목")
        String experienceTitle,

        @Schema(description = "예약 날짜")
        LocalDate reservedDate,

        @Schema(description = "예약 시작 시간")
        LocalTime reservedStartTime,

        @Schema(description = "예약 종료 시간")
        LocalTime reservedEndTime,

        @Schema(description = "총 참석 인원")
        int totalParticipants
) {
    public static HostReservationResponseDto from(Reservation reservation) {
        LocalTime endTime = reservation.getReservedStartTime()
                .toLocalTime()
                .plusHours(reservation.getExperience().getDurationInHours());

        return new HostReservationResponseDto(
                reservation.getId(),
                reservation.getExperience().getId(),
                reservation.getExperience().getTitle(),
                reservation.getReservedStartTime().toLocalDate(),
                reservation.getReservedStartTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES),
                endTime,
                reservation.getTotalParticipants()
        );
    }
}