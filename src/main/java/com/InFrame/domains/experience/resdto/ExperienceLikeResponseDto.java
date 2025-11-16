package com.InFrame.domains.experience.resdto;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "체험 좋아요 정보 응답 DTO")
public record ExperienceLikeResponseDto(
        @Schema(description = "체험 ID")
        Long experienceId,

        @Schema(description = "체험 제목")
        String title,

        @Schema(description = "호스트 이름")
        String hostName,

        @Schema(description = "가격")
        int price,

        @Schema(description = "평균 평점")
        double rating,

        @Schema(description = "체험 이미지 URL 목록")
        List<String> imageUrls
) {
    public static ExperienceLikeResponseDto from(
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

        return new ExperienceLikeResponseDto(
                experience.getId(),
                experience.getTitle(),
                host.getUser().getName(),
                experience.getPrice(),
                rating,
                experience.getImageUrls()
        );
    }
}
