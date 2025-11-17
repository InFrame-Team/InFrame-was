package com.InFrame.domains.reservation.repository;

import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.reservation.entity.Reservation;
import com.InFrame.domains.reservation.entity.enums.ReservationStatus;
import com.InFrame.domains.review.entity.Review;
import com.InFrame.domains.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 특정 체험에 대해 특정 날짜에 이미 예약된 시작 시간 목록을 조회
    @Query("SELECT r.reservedStartTime FROM Reservation r " +
            "WHERE r.experience.id = :experienceId " +
            "AND r.reservedStartTime BETWEEN :startOfDay AND :endOfDay " +
            "AND r.status != com.InFrame.domains.reservation.entity.enums.ReservationStatus.CANCELLED") // [수정]
    List<LocalDateTime> findReservedStartTimesByExperienceIdAndDate(
            @Param("experienceId") Long experienceId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    // 유저가 예약한 내역 리스트
    @Query("SELECT DISTINCT r FROM Reservation r " +
            "JOIN FETCH r.experience exp " +
            "JOIN FETCH exp.host h " +
            "JOIN FETCH h.user " + // 호스트 본명, 프로필 이미지용
            "LEFT JOIN FETCH exp.imageUrls " + // 체험 이미지용 (없을 수 있으므로 LEFT JOIN)
            "WHERE r.user = :user " +
            "ORDER BY r.reservedStartTime DESC")
    List<Reservation> findAllByUserOrderByReservedStartTimeDesc(@Param("user") User user);

    // 특정 체험에 대해 특정 시작 시간에 유효한 예약이 존재하는지 확인
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r " +
            "WHERE r.experience.id = :experienceId " +
            "AND r.reservedStartTime = :startTime " +
            "AND r.status != com.InFrame.domains.reservation.entity.enums.ReservationStatus.CANCELLED")
    boolean existsValidReservationAtTime(
            @Param("experienceId") Long experienceId,
            @Param("startTime") LocalDateTime startTime
    );

    // 예약 목록으로 리뷰 목록을 한 번에 조회
    @Query("SELECT r FROM Review r WHERE r.reservation IN :reservations")
    List<Review> findAllByReservationIn(@Param("reservations") List<Reservation> reservations);

    // 특정 체험에 대해 특정 시작 시간에 예약이 존재하는지 확인 (중복 예약 방지용)
    boolean existsByExperienceIdAndReservedStartTime(Long experienceId, LocalDateTime startTime);

    List<Reservation> findAllByStatus(ReservationStatus status);

    // 유저가 예약한 내역 수
    long countByUser(User user);

    // 특정 호스트의 모든 체험에 대해 특정 상태의 예약 수
    long countByExperience_HostAndStatus(Host host, ReservationStatus status);
}
