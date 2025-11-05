package com.InFrame.domains.experience.resdto;

import lombok.Getter;

@Getter
public class EnumResponseDto {
    private final String code;
    private final String description;

    public EnumResponseDto(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
