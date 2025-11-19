package com.InFrame.domains.review.controller.api;

import com.InFrame.domains.review.reqdto.ReviewRequestDto;
import com.InFrame.domains.review.resdto.ReviewResponseDto;
import com.InFrame.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "Review", description = "리뷰 API")
public interface ReviewApi {

    @Operation(
            summary = "리뷰 작성",
            description = "체험 완료 상태의 예약에 대해 리뷰를 작성합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "리뷰 작성 성공",
                            content = @Content(schema = @Schema(implementation = ReviewResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "예약자 본인이 아님"),
                    @ApiResponse(responseCode = "409", description = "이미 리뷰를 작성했거나, 아직 체험 완료 상태가 아님")
            }
    )
    ResponseEntity<?> createReview(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails,

            @PathVariable Long reservationId,

            @Valid
            @RequestPart(name = "reviewRequestDto")
            @Parameter(
                    name = "reviewRequestDto",
                    description = "리뷰 본문 DTO",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReviewRequestDto.class)
                    )
            )
            ReviewRequestDto reviewRequestDto,

            @RequestPart(name = "reviewImage", required = false)
            @Parameter(
                    name = "reviewImage",
                    description = "리뷰 이미지 파일",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            MultipartFile reviewImage
    );


    @Operation(summary = "특정 체험의 리뷰 목록 조회",
            description = "experienceId에 해당하는 체험의 모든 리뷰를 조회합니다.",
            responses = @ApiResponse(responseCode = "200", description = "조회 성공"))
    @GetMapping("/experience/{experienceId}")
    ResponseEntity<?> getReviewsByExperience(
            @PathVariable Long experienceId
    );

    @Operation(summary = "특정 호스트의 리뷰 목록 조회",
            description = "hostId에 해당하는 호스트의 모든 체험에 달린 리뷰를 조회합니다.",
            responses = @ApiResponse(responseCode = "200", description = "조회 성공"))
    @GetMapping("/host/{hostId}")
    ResponseEntity<?> getReviewsByHost(
            @PathVariable Long hostId
    );

    @Operation(summary = "리뷰 삭제",
            description = "reviewId에 해당하는 리뷰를 삭제합니다. (작성자 본인만 가능)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "삭제 성공"),
                    @ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
                    @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음")
            })
    @DeleteMapping("/{reviewId}")
    ResponseEntity<?> deleteReview(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reviewId
    );
}