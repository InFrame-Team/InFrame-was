package com.InFrame.domains.review.resdto;

import com.InFrame.domains.review.entity.Review;

import java.time.LocalDateTime;

public record ReviewResponseDto(
        Long reviewId,
        String writerNickname,
        String writerProfileImageUrl,
        String hostName,
        Long experienceId,
        String experienceTitle,
        int rating,
        String comment,
        String reviewImageUrl,
        LocalDateTime createdAt
) {
    public static ReviewResponseDto from(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getUser().getNickname(),
                review.getUser().getProfileImageUrl(),
                review.getReservation().getExperience().getHost().getUser().getName(),
                review.getReservation().getExperience().getId(),
                review.getReservation().getExperience().getTitle(),
                review.getRating(),
                review.getComment(),
                review.getReviewImageUrl(),
                review.getCreatedAt()
        );
    }
}
