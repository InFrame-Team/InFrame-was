package com.InFrame.domains.review.controller;

import com.InFrame.domains.review.controller.api.ReviewApi;
import com.InFrame.domains.review.reqdto.ReviewRequestDto;
import com.InFrame.domains.review.resdto.ReviewResponseDto;
import com.InFrame.domains.review.service.ReviewService;
import com.InFrame.security.userdetails.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController implements ReviewApi {
    private final ReviewService reviewService;
    private final ObjectMapper objectMapper;

    @Hidden
    public ResponseEntity<?> createReviewRaw(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reservationId,
            @RequestParam("reviewRequestDto") String reviewRequestDtoJson,
            @RequestPart(name = "reviewImage", required = false) MultipartFile reviewImage
    ) throws JsonProcessingException {

        ReviewRequestDto dto = objectMapper.readValue(reviewRequestDtoJson, ReviewRequestDto.class);

        return createReview(userDetails, reservationId, dto, reviewImage);
    }

    @Override
    @PostMapping(
            value = "/{reservationId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reservationId,
            @RequestPart(name = "reviewRequestDto") ReviewRequestDto reviewRequestDto,
            @RequestPart(name = "reviewImage", required = false) MultipartFile reviewImage
    ) {
        ReviewResponseDto responseDto = reviewService.createReview(
                reservationId,
                reviewRequestDto,
                reviewImage,
                userDetails.getUser()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @Override
    @GetMapping("/experience/{experienceId}")
    public ResponseEntity<?> getReviewsByExperience(
            @PathVariable Long experienceId
    ) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByExperience(experienceId);
        return ResponseEntity.ok(reviews);
    }

    @Override
    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByHost(
            @PathVariable Long hostId
    ) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByHost(hostId);
        return ResponseEntity.ok(reviews);
    }

    @Override
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(reviewId, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}
