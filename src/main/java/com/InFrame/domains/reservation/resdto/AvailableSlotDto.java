package com.InFrame.domains.reservation.resdto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

@Schema(description = "예약 가능 시간 슬롯 정보 DTO")
public record AvailableSlotDto(
        @Schema(description = "시작 시간 (HH:mm)")
        LocalTime startTime,

        @Schema(description = "예약 가능 여부 (true : 가능, false : 마감)")
        boolean isAvailable
) {}
