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
    HOST_INFO_NOT_FOUND(404, "호스트 정보가 등록되어 있지 않습니다."),

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
    RESERVATION_CAPACITY_EXCEEDED(400, "최대 예약 가능 인원을 초과했습니다."),
    RESERVATION_NOT_FOUND(404, "해당 예약을 찾을 수 없습니다."),

    // 이미지 관련 오류
    FILE_IS_EMPTY(404, "파일이 비어있습니다."),
    FILE_UPLOAD_FAILED(400, "파일 업로드에 실패하였습니다."),
    INVALID_FILE_URL(400, "잘못된 경로입니다."),

    // 리뷰 관련 오류
    REVIEW_NOT_ALLOWED(403, "체험 완료 상태에서만 리뷰를 작성할 수 있습니다."),
    REVIEW_ALREADY_EXISTS(409, "이미 리뷰를 작성한 체험입니다."),
    REVIEW_NOT_FOUND(404, "해당 리뷰를 찾을 수 없습니다."),

    // 사업자 번호 조회 관련 오류
    INVALID_BUSINESS_NUMBER_FORMAT(400, "사업자 번호 형식이 올바르지 않습니다."),
    INVALID_BUSINESS_NUMBER(400, "국세청에 등록되지 않은 사업자 번호입니다."),
    INACTIVE_BUSINESS_NUMBER(400, "휴업 또는 폐업 상태의 사업자입니다."),
    BUSINESS_API_FAILED(500, "사업자 번호 조회 API 호출에 실패했습니다.");
    private final int status;
    private final String message;
}