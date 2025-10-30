package com.InFrame.common.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 서버 관련 오류
    INTERNAL_SERVER_ERROR(500, "내부 서버 오류입니다."),

    // 유저 관련 오류
    USER_NOT_FOUND(404, "해당 유저를 찾을 수 없습니다."),
    FORBIDDEN_ACCESS(403, "해당 유저는 권한이 없습니다."),
    HOST_NOT_FOUND(404, "해당 유저의 호스트 정보를 찾을 수 없습니다."),

    // 회원가입, 로그인 오류
    EMAIL_ALREADY_EXIST(409, "해당 이메일은 이미 존재합니다."),
    NICKNAME_ALREADY_EXIST(409, "해당 닉네임은 이미 존재합니다."),
    INCORRECT_PASSWORD(400, "비밀번호가 일치하지 않습니다."),

    // 호스트 변경 관련 오류
    USER_ALREADY_HOST(409, "이미 호스트로 등록된 유저입니다."),
    BUSINESS_NUMBER_ALREADY_EXISTS(409, "이미 등록된 사업자 번호입니다."),

    // 체험 관련 오류
    EXPERIENCE_NOT_FOUND(404, "해당 체험을 찾을 수 없습니다."),

    // 예약 관련 오류
    RESERVATION_SLOT_NOT_AVAILABLE(409, "이미 예약이 마감된 시간입니다."),
    INVALID_PARTICIPANT_COUNT(400, "예약 인원은 1명 이상이어야 합니다."),
    RESERVATION_ON_CLOSED_DAY(400, "체험 휴무일에는 예약할 수 없습니다."),
    INVALID_RESERVATION_TIME(400, "선택한 시간은 예약 가능한 시간이 아닙니다."),
    RESERVATION_CAPACITY_EXCEEDED(400, "최대 예약 가능 인원을 초과했습니다.");

    private final int status;
    private final String message;
}