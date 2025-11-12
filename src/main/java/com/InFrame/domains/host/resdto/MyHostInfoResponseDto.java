package com.InFrame.domains.host.resdto;

import com.InFrame.domains.host.entity.Host;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "내 호스트 정보 응답 DTO")
public record MyHostInfoResponseDto(
        @Schema(description = "호스트 본명")
        String hostName,

        @Schema(description = "호스트 프로필 이미지")
        String profileImageUrl,

        @Schema(description = "호스트 소개")
        String description
) {
    public static MyHostInfoResponseDto from(Host host) {
        return new MyHostInfoResponseDto(
                host.getUser().getName(),
                host.getUser().getProfileImageUrl(),
                host.getDescription()
        );
    }
}