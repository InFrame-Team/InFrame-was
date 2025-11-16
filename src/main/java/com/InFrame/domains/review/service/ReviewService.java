package com.InFrame.domains.review.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import com.InFrame.common.service.S3UploadService;
import com.InFrame.domains.experience.repository.ExperienceRepository;
import com.InFrame.domains.host.repository.HostRepository;
import com.InFrame.domains.reservation.entity.Reservation;
import com.InFrame.domains.reservation.entity.enums.ReservationStatus;
import com.InFrame.domains.reservation.repository.ReservationRepository;
import com.InFrame.domains.review.entity.Review;
import com.InFrame.domains.review.repository.ReviewRepository;
import com.InFrame.domains.review.reqdto.ReviewRequestDto;
import com.InFrame.domains.review.resdto.ReviewResponseDto;
import com.InFrame.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final S3UploadService s3UploadService;
    private final ExperienceRepository experienceRepository;
    private final HostRepository hostRepository;


    // 리뷰 생성
    public ReviewResponseDto createReview(
            Long reservationId,
            ReviewRequestDto dto,
            MultipartFile reviewImage,
            User user
    ) {
        // 1. 예약 정보 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        // 2. 예약자 본인인지 확인
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 3. 체험 완료 상태인지 확인
        if (reservation.getStatus() != ReservationStatus.COMPLETED) {
            throw new CustomException(ErrorCode.REVIEW_NOT_ALLOWED);
        }

        // 4. 중복 리뷰 검증
        if (reviewRepository.findByReservation(reservation).isPresent()) { // reservationId 대신 reservation 객체 사용
            throw new CustomException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        // 5. S3에 이미지 업로드
        String imageUrl = null;
        if (reviewImage != null && !reviewImage.isEmpty()) {
            imageUrl = s3UploadService.uploadFile(reviewImage, "reviews");
        }

        Review review = dto.toEntity(user, reservation, imageUrl);

        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.from(savedReview);
    }

    // 특정 체험의 모든 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByExperience(Long experienceId) {
        if (!experienceRepository.existsById(experienceId)) {
            throw new CustomException(ErrorCode.EXPERIENCE_NOT_FOUND);
        }

        return reviewRepository.findAllDtoByExperienceId(experienceId);
    }

    // 특정 호스트의 모든 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByHost(Long hostId) {
        // Host가 존재하는지 확인 (선택적)
        if (!hostRepository.existsById(hostId)) {
            throw new CustomException(ErrorCode.HOST_NOT_FOUND);
        }

        return reviewRepository.findAllDtoByHostId(hostId);
    }

    public void deleteReview(Long reviewId, User user) {
        // 1. 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        // 2. 리뷰 작성자 본인인지 확인
        if (!review.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        String imageUrl = review.getReviewImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            s3UploadService.deleteFile(imageUrl);
        }

        reviewRepository.delete(review);
    }
}