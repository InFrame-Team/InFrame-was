package com.InFrame.domains.host.resdto;

import com.InFrame.domains.experience.entity.enums.DetailField;
import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.host.entity.enums.Category;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "지도 표시용 호스트 정보 응답 DTO")
public record HostMapResponseDto(
        @Schema(description = "호스트 ID")
        Long hostId,
        @Schema(description = "유저 ID")
        Long userId,
        @Schema(description = "사업자명")
        String businessName,
        @Schema(description = "호스트 본명")
        String hostName,
        @Schema(description = "호스트 프로필 이미지")
        String profileImageUrl,
        @Schema(description = "분야 카테고리")
        Category category,
        @Schema(description = "체험 세부 분야", nullable = true)
        DetailField detailField,
        @Schema(description = "위도")
        Double latitude,
        @Schema(description = "경도")
        Double longitude,
        @Schema(description = "기본 주소")
        String addressBase,
        @Schema(description = "업체 로고 이미지 URL")
        String companyLogoUrl,
        @Schema(description = "호스트가 받은 총 리뷰 수")
        long reviewCount,
        @Schema(description = "해당 호스트의 체험 중 최저가", nullable = true)
        Integer lowestPrice
) {
    public static HostMapResponseDto from(Host host, long reviewCount, DetailField detailField, Integer lowestPrice) {
        return new HostMapResponseDto(
                host.getId(),
                host.getUser().getId(),
                host.getBusinessName(),
                host.getUser().getName(),
                host.getUser().getProfileImageUrl(),
                host.getCategory(),
                detailField,
                host.getLatitude(),
                host.getLongitude(),
                host.getAddressBase(),
                host.getCompanyLogoUrl(),
                reviewCount,
                lowestPrice
        );
    }
}