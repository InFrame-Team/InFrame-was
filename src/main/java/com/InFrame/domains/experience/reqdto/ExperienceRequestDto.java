package com.InFrame.domains.experience.reqdto;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.experience.entity.enums.DetailField;
import com.InFrame.domains.experience.entity.enums.ProfessionalField;
import com.InFrame.domains.host.entity.Host;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Schema(description = "체험 생성 요청 DTO")
public record ExperienceRequestDto (
        @Schema(description = "전문분야")
        @NotNull(message = "전문분야는 필수입니다.")
        ProfessionalField professionalField,

        @Schema(description = "상세분야")
        @NotNull(message = "상세분야는 필수입니다.")
        DetailField detailField,

        @Schema(description = "보유한 자격증", example = "한식조리기능사, 바리스타 1급")
        String certifications,

        @Schema(description = "업체 정보", example = "동민카페")
        String companyInfo,

        @Schema(description = "체험 제목", example = "한 입의 예술, 핸드메이드 초콜릿")
        @NotBlank(message = "체험 제목은 필수 입력입니다.")
        String title,

        @Schema(description = "체험 설명", example = "저희 체험은 100년 째 이어지고 있는 머시기 ,, (생략)")
        @NotBlank(message = "체험 설명은 필수 입력입니다.")
        String description,

        @Schema(description = "체험 가격", example = "50000")
        @NotNull(message = "체험 가격은 필수 입력입니다.")
        Integer price,

        @Schema(description = "체험 시간", example = "3")
        @NotNull(message = "체험 시간은 필수 입력입니다.")
        Integer durationInHours,

        @Schema(description = "최대 예약 가능 인원")
        Integer maxCapacityPerSlot,

        @NotEmpty(message = "예약 가능 요일은 최소 하나 이상 선택해야 합니다.")
        @Schema(description = "예약 가능 요일 목록 (영문 대문자)", example = "[\"MONDAY\", \"SUNDAY\"]")
        Set<DayOfWeek> availableDaysOfWeek,

        @NotEmpty(message = "예약 가능 시간은 최소 하나 이상 선택해야 합니다.")
        @Schema(description = "예약 가능한 시작 시간 목록 (HH:mm 형식)", example = "[\"09:00\", \"11:00\", \"14:00\"]")
        Set<LocalTime> availableTimes
){
    public Experience toEntity(Host host) {
        return Experience.builder()
                .host(host)
                .professionalField(professionalField)
                .detailField(detailField)
                .certifications(certifications)
                .companyInfo(companyInfo)
                .title(title)
                .description(description)
                .price(price)
                .durationInHours(durationInHours)
                .maxCapacityPerSlot(maxCapacityPerSlot)
                .availableDaysOfWeek(availableDaysOfWeek)
                .availableTimes(availableTimes)
                .build();
    }
}
