package com.InFrame.domains.review.repository;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.review.entity.Review;
import com.InFrame.domains.review.resdto.ReviewResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 호스트의 리뷰 개수를 반환
    long countByReservation_Experience_Host(Host host);

    // 예약 ID로 리뷰가 이미 작성되었는지 확인
    Optional<Review> findByReservationId(Long reservationId);

    // 좋아요 조회 기능용
    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.reservation res " +
            "JOIN FETCH res.experience exp " +
            "WHERE exp IN :experiences")
    List<Review> findAllByExperienceInWithExperience(@Param("experiences") List<Experience> experiences);

    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.user u " + // DTO 변환 시 N+1 방지
            "JOIN r.reservation res " +
            "WHERE res.experience.id = :experienceId")
    List<Review> findAllByExperienceId(@Param("experienceId") Long experienceId);

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