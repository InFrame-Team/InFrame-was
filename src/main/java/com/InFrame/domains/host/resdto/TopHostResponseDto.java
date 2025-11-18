package com.InFrame.domains.host.resdto;

import com.InFrame.domains.host.entity.Host;
import io.swagger.v3.oas.annotations.media.Schema;

public record TopHostResponseDto(
        @Schema(description = "호스트 ID")
        Long hostId,

        @Schema(description = "호스트 이름")
        String hostName,

        @Schema(description = "호스트 프로필 이미지")
        String hostProfileImg,

        @Schema(description = "후기 개수")
        long reviewCount,

        @Schema(description = "후기 평점")
        double averageRating

) {
    public static TopHostResponseDto from(Host host, long reviewCount, double averageRating) {
        return new TopHostResponseDto(
                host.getId(),
                host.getBusinessName(),
                host.getCompanyLogoUrl(),
                reviewCount,
                averageRating
        );
    }
}
