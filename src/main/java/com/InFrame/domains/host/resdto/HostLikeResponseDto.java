package com.InFrame.domains.host.resdto;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.host.entity.Host;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "호스트 좋아요 정보 응답 DTO")
public record HostLikeResponseDto(
        @Schema(description = "호스트 ID")
        Long hostId,

        @Schema(description = "호스트 본명")
        String hostName,

        @Schema(description = "호스트 프로필 이미지")
        String profileImageUrl,

        @Schema(description = "호스트가 등록한 체험 이미지 URL 목록")
        List<String> experienceImageUrls
) {
    public static HostLikeResponseDto from(
            Host host,
            List<Experience> experiences
    ) {
        List<String> allImages = experiences.stream()
                .map(Experience::getImageUrls)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return new HostLikeResponseDto(
                host.getId(),
                host.getUser().getName(),
                host.getUser().getProfileImageUrl(),
                allImages
        );
    }
}
