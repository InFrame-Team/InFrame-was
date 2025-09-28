package com.InFrame.domains.user.reqdto;

public record SignInRequestDto(
        String email,
        String password
) {
}
