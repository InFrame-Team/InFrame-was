package com.InFrame.domains.review.reqdto;

import com.InFrame.domains.reservation.entity.Reservation;
import com.InFrame.domains.review.entity.Review;
import com.InFrame.domains.user.entity.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewRequestDto(
        @NotNull(message = "별점은 필수입니다.")
        @Min(value = 1, message = "별점은 1점 이상이어야 합니다.")
        @Max(value = 5, message = "별점은 5점 이하이어야 합니다.")
        int rating,

        @NotBlank(message = "후기 내용은 필수입니다.")
        String comment
) {
    public Review toEntity(User user, Reservation reservation,String reviewImageUrl) {
        return Review.builder()
                .user(user)
                .reservation(reservation)
                .rating(rating)
                .comment(comment)
                .reviewImageUrl(reviewImageUrl)
                .build();
    }
}
