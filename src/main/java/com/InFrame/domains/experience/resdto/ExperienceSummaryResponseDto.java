package com.InFrame.domains.experience.resdto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "체험 목록 요약 정보 응답 DTO")
public record ExperienceSummaryResponseDto(
        @Schema(description = "체험 ID")
        Long experienceId,

        @Schema(description = "체험 대표 이미지 URL")
        String mainImageUrl,

        @Schema(description = "체험 이름")
        String title,

        @Schema(description = "체험 설명")
        String description,

        @Schema(description = "가격")
        int price,

        @Schema(description = "평균 평점 (0.0 ~ 5.0)")
        Double rating,

        @Schema(description = "소요 시간 (시간 단위)")
        int durationInHours,

        @Schema(description = "리뷰 총 개수")
        Long reviewCount,

        @Schema(description = "사용자의 좋아요 상태")
        boolean isLiked
) {
}