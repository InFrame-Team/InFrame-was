package com.InFrame.domains.host.reqdto;

import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "호스트로 변경 요청 DTO")
public record HostRequestDto(
        @Schema(description = "사업자 번호", example = "1234567890")
        @NotBlank(message = "사업자 번호는 필수 입력입니다.")
        String businessNumber,

        @Schema(description = "사업자명", example = "브릿지")
        @NotBlank(message = "사업자명은 필수 입력입니다.")
        String businessName,

        @Schema(description = "고객센터 전화번호", example = "111-1111")
        @NotBlank(message = "고객센터 전화번호는 필수 입력입니다.")
        String businessPhoneNumber,

        @Schema(description = "고객센터 이메일", example = "abcd1234@gmail.com")
        @Email(message = "이메일 형식을 맞춰주세요.")
        String businessEmail,

        @Schema(description = "카카오톡 채널 주소", example = "https://pf.kakao.com/채널주소")
        String kakaoAddress
) {
    public Host toEntity(User user) {
        return Host.builder()
                .user(user)
                .businessNumber(businessNumber)
                .businessName(businessName)
                .businessPhoneNumber(businessPhoneNumber)
                .businessEmail(businessEmail)
                .kakaoAddress(kakaoAddress)
                .build();
    }
}
