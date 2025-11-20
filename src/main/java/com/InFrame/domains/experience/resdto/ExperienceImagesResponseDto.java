package com.InFrame.domains.experience.resdto;

import com.InFrame.domains.experience.entity.Experience;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "호스트 체험의 이미지 목록 응답 DTO")
public record ExperienceImagesResponseDto(
        @Schema(description = "체험 ID")
        Long experienceId,

        @Schema(description = "체험 이미지 URL 목록")
        List<String> imageUrls
) {
    public static ExperienceImagesResponseDto from(Experience experience) {
        return new ExperienceImagesResponseDto(
                experience.getId(),
                experience.getImageUrls()
        );
    }
}