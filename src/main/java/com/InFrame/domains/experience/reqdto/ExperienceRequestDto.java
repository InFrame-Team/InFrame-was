package com.InFrame.domains.experience.reqdto;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.host.entity.Host;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "체험 생성 요청 DTO")
public record ExperienceRequestDto (
        @Schema(description = "체험 제목", example = "한 입의 예술, 핸드메이드 초콜릿")
        @NotBlank(message = "체험 제목은 필수 입력입니다.")
        String title,

        @Schema(description = "체험 소개", example = "저희 체험은 100년 째 이어지고 있는 머시기 ,, (생략)")
        @NotBlank(message = "체험 소개는 필수 입력입니다.")
        String description,

        @Schema(description = "체험 가격", example = "50000")
        @NotNull(message = "체험 가격은 필수 입력입니다.")
        int price,

        @Schema(description = "체험 시간", example = "3")
        @NotNull(message = "체험 시간은 필수 입력입니다.")
        int time
){
    public Experience toEntity(Host host) {
        return Experience.builder()
                .host(host)
                .title(title)
                .description(description)
                .price(price)
                .time(time)
                .build();
    }
}
