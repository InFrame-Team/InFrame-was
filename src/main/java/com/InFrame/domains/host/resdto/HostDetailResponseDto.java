package com.InFrame.domains.host.resdto;

import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

@Schema(description = "특정 호스트의 상세 정보 응답 DTO")
public record HostDetailResponseDto(
        @Schema(description = "유저 ID")
        Long userId,

        @Schema(description = "유저 이름")
        String name,

        @Schema(description = "유저 프로필 이미지 URL")
        String profileImageUrl,

        @Schema(description = "호스트 ID")
        Long hostId,

        @Schema(description = "사업자명")
        String businessName,

        @Schema(description = "고객센터 전화번호")
        String businessPhoneNumber,

        @Schema(description = "고객센터 이메일")
        String businessEmail,

        @Schema(description = "카카오톡 채널 주소")
        String kakaoAddress,

        @Schema(description = "호스트 소개")
        String description,

        @Schema(description = "위도")
        Double latitude,

        @Schema(description = "경도")
        Double longitude,

        @Schema(description = "기본 주소")
        String addressBase,

        @Schema(description = "상세 주소")
        String addressDetail,

        @Schema(description = "연락 가능 시작 시간")
        LocalTime contactStartTime,

        @Schema(description = "연락 가능 종료 시간")
        LocalTime contactEndTime,

        @Schema(description = "취소 정책")
        String cancellationPolicy,

        @Schema(description = "회사 로고 URL")
        String companyLogoUrl,

        @Schema(description = "자세한 소개")
        String detailedDescription,

        @Schema(description = "총 후기 수")
        Long reviewCount,

        @Schema(description = "평점 (0.0 ~ 5.0)")
        Double averageRating
) {
    public static HostDetailResponseDto from(User user, Host host, Long reviewCount, Double averageRating) {
        return new HostDetailResponseDto(
                user.getId(),
                user.getName(),
                user.getProfileImageUrl(),
                host.getId(),
                host.getBusinessName(),
                host.getBusinessPhoneNumber(),
                host.getBusinessEmail(),
                host.getKakaoAddress(),
                host.getDescription(),
                host.getLatitude(),
                host.getLongitude(),
                host.getAddressBase(),
                host.getAddressDetail(),
                host.getContactStartTime(),
                host.getContactEndTime(),
                host.getCancellationPolicy(),
                host.getCompanyLogoUrl(),
                host.getDetailedDescription(),
                reviewCount,
                averageRating
        );
    }
}