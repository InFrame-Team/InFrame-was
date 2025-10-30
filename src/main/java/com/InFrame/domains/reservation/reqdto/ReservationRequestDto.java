package com.InFrame.domains.reservation.reqdto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "예약 생성 요청 DTO")
public record ReservationRequestDto(
        @NotNull(message = "체험 ID는 필수입니다.")
        @Schema(description = "예약할 체험 ID")
        Long experienceId,

        @NotNull(message = "예약 날짜는 필수입니다.")
        @FutureOrPresent(message = "과거 날짜는 예약할 수 없습니다.")
        @Schema(description = "예약 날짜 (YYYY-MM-DD)", example = "2025-11-10")
        LocalDate reservationDate,

        @NotNull(message = "시작 시간은 필수입니다.")
        @Schema(description = "예약 시작 시간 (HH:mm)", example = "09:00")
        LocalTime startTime,

        @NotNull(message = "성인 인원은 필수입니다.")
        @Min(value = 0, message = "성인 인원은 0명 이상이어야 합니다.")
        @Schema(description = "예약할 성인 인원수", example = "2")
        Integer numAdults,

        @NotNull(message = "아동 인원은 필수입니다.")
        @Min(value = 0, message = "아동 인원은 0명 이상이어야 합니다.")
        @Schema(description = "예약할 아동 인원수", example = "0")
        Integer numChildren
) {}