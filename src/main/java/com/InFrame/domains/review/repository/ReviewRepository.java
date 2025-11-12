package com.InFrame.domains.review.repository;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.review.entity.Review;
import com.InFrame.domains.review.resdto.ReviewResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 예약 ID로 리뷰가 이미 작성되었는지 확인
    Optional<Review> findByReservationId(Long reservationId);

    // 특정 체험에 달린 모든 리뷰를 DTO로 직접 조회
    @Query("SELECT new com.InFrame.domains.review.resdto.ReviewResponseDto(" +
            "r.id, " +
            "u.nickname, " +
            "hu.name, " +
            "exp.id, " +
            "exp.title, " +
            "r.rating, " +
            "r.comment, " +
            "r.reviewImageUrl, " +
            "r.createdAt) " +
            "FROM Review r " +
            "JOIN r.user u " +
            "JOIN r.reservation res " +  //
            "JOIN res.experience exp " + //
            "JOIN exp.host h " +         //
            "JOIN h.user hu " +          //
            "WHERE exp.id = :experienceId")
    List<ReviewResponseDto> findAllDtoByExperienceId(@Param("experienceId") Long experienceId);

    // 특정 호스트에게 달린 모든 리뷰를 DTO로 직접 조회
    @Query("SELECT new com.InFrame.domains.review.resdto.ReviewResponseDto(" +
            "r.id, " +
            "u.nickname, " +
            "hu.name, " +
            "exp.id, " +
            "exp.title, " +
            "r.rating, " +
            "r.comment, " +
            "r.reviewImageUrl, " +
            "r.createdAt) " +
            "FROM Review r " +
            "JOIN r.user u " +
            "JOIN r.reservation res " +
            "JOIN res.experience exp " +
            "JOIN exp.host h " +
            "JOIN h.user hu " +
            "WHERE h.id = :hostId")
    List<ReviewResponseDto> findAllDtoByHostId(@Param("hostId") Long hostId);
}