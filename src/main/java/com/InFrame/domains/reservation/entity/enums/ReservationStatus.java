package com.InFrame.domains.reservation.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    RESERVED("예약 완료"),
    COMPLETED("체험 완료"),
    CANCELLED("예약 취소");

    private final String description;
}
