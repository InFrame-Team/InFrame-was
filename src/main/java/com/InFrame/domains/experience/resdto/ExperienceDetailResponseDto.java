package com.InFrame.domains.experience.resdto;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.experience.entity.enums.DetailField;
import com.InFrame.domains.experience.entity.enums.ProfessionalField;
import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.List;

@Schema(description = "체험 상세 정보 응답 DTO")
public record ExperienceDetailResponseDto(
        @Schema(description = "체험 ID")
        Long experienceId,

        @Schema(description = "체험 제목")
        String title,

        @Schema(description = "호스트 이미지")
        String hostProfile,

        @Schema(description = "호스트 이름")
        String hostName,

        @Schema(description = "호스트 자기소개")
        String hostIntro,

        @Schema(description = "체험 소요시간")
        int durationInHours,

        @Schema(description = "체험 소개")
        String experienceIntro,

        @Schema(description = "전문 분야")
        ProfessionalField professionalField,

        @Schema(description = "세부 분야")
        DetailField detailField,

        @Schema(description = "장소")
        String location,

        @Schema(description = "가격")
        int price,

        @Schema(description = "평균 평점")
        double rating,

        @Schema(description = "리뷰 총 개수")
        int reviewCount,

        @Schema(description = "체험 이미지 URL 목록")
        List<String> imageUrls,

        @Schema(description = "체험 유의사항")
        String caution,

        @Schema(description = "연락 가능 시작 시간")
        LocalTime contactStartTime,

        @Schema(description = "연락 가능 종료 시간")
        LocalTime contactEndTime,

        @Schema(description = "고객센터 전화번호")
        String businessPhoneNumber
) {
    public static ExperienceDetailResponseDto from(
            Experience experience,
            Host host,
            List<Review> reviewEntities
    ) {
        int reviewCount = reviewEntities.size();
        double rating = 0.0;
        if (reviewCount > 0) {
            rating = reviewEntities.stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
            rating = Math.round(rating * 10.0) / 10.0;
        }

        return new ExperienceDetailResponseDto(
                experience.getId(),
                experience.getTitle(),
                host.getUser().getProfileImageUrl(),
                host.getUser().getName(),
                host.getDescription(),
                experience.getDurationInHours(),
                experience.getDescription(),
                experience.getProfessionalField(),
                experience.getDetailField(),
                host.getAddressBase(),
                experience.getPrice(),
                rating,
                reviewCount,
                experience.getImageUrls(),
                experience.getCaution(),
                host.getContactStartTime(),
                host.getContactEndTime(),
                host.getBusinessPhoneNumber()
        );
    }
}