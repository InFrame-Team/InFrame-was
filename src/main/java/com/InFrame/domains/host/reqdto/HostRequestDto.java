package com.InFrame.domains.host.reqdto;

import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.host.entity.enums.Category;
import com.InFrame.domains.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@Schema(description = "호스트로 변경 요청 DTO")
public record HostRequestDto(
        @Schema(description = "사업자 번호", example = "1234567890")
        @NotBlank(message = "사업자 번호는 필수 입력입니다.")
        String businessNumber,

        @Schema(description = "분야 카테고리")
        @NotNull(message = "분야 카테고리는 필수입니다.")
        Category category,

        @Schema(description = "사업자명", example = "브릿지")
        @NotBlank(message = "사업자명은 필수 입력입니다.")
        String businessName,

        @Schema(description = "고객센터 전화번호", example = "010-0000-0000")
        @NotBlank(message = "고객센터 전화번호는 필수 입력입니다.")
        String businessPhoneNumber,

        @Schema(description = "고객센터 이메일", example = "inGyeongsan@yu.ac.kr")
        @Email(message = "이메일 형식을 맞춰주세요.")
        String businessEmail,

        @Schema(description = "카카오톡 채널 주소", example = "https://pf.kakao.com/채널주소")
        String kakaoAddress,

        @Schema(description = "호스트 소개", example = "흙을 담아 삶의 이야기를 빚어냅니다.")
        String description,

        @Schema(description = "위도", example = "37.5665")
        @NotNull(message = "위도는 필수입니다.")
        Double latitude,

        @Schema(description = "경도", example = "126.9780")
        @NotNull(message = "경도는 필수입니다.")
        Double longitude,

        @Schema(description = "기본 주소 (도로명/지번)", example = "서울 중구 세종대로 110")
        @NotBlank(message = "기본 주소는 필수입니다.")
        String addressBase,

        @Schema(description = "상세 주소 (선택)", example = "2층")
        String addressDetail,

        @Schema(description = "연락 가능 시작 시간 (HH:mm)", example = "09:00")
        LocalTime contactStartTime,

        @Schema(description = "연락 가능 종료 시간 (HH:mm)", example = "18:00")
        LocalTime contactEndTime,

        @Schema(description = "취소 및 환불 정책")
        String cancellationPolicy,

        @Schema(description = "자세한 소개")
        String detailedDescription
) {
    public Host toEntity(User user) {
        return Host.builder()
                .user(user)
                .businessNumber(businessNumber)
                .category(category)
                .businessName(businessName)
                .businessPhoneNumber(businessPhoneNumber)
                .businessEmail(businessEmail)
                .kakaoAddress(kakaoAddress)
                .description(description)
                .latitude(latitude)
                .longitude(longitude)
                .addressBase(addressBase)
                .addressDetail(addressDetail)
                .contactStartTime(contactStartTime)
                .contactEndTime(contactEndTime)
                .cancellationPolicy(cancellationPolicy)
                .detailedDescription(detailedDescription)
                .companyLogoUrl(null)
                .build();
    }
}
