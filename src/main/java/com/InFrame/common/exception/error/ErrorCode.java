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

    // 회원가입, 로그인 오류
    EMAIL_ALREADY_EXIST(409, "해당 이메일은 이미 존재합니다."),
    NICKNAME_ALREADY_EXIST(409, "해당 닉네임은 이미 존재합니다."),
    INCORRECT_PASSWORD(400, "비밀번호가 일치하지 않습니다.");

    private final int status;
    private final String message;
}
