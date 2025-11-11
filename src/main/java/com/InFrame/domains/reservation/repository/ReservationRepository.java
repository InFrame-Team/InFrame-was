package com.InFrame.domains.reservation.repository;

import com.InFrame.domains.reservation.entity.Reservation;
import com.InFrame.domains.reservation.entity.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 특정 체험에 대해 특정 날짜에 이미 예약된 시작 시간 목록을 조회
    @Query("SELECT r.reservedStartTime FROM Reservation r " +
            "WHERE r.experience.id = :experienceId " +
            "AND r.reservedStartTime BETWEEN :startOfDay AND :endOfDay")
    List<LocalDateTime> findReservedStartTimesByExperienceIdAndDate(
            @Param("experienceId") Long experienceId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    // 특정 체험에 대해 특정 시작 시간에 예약이 존재하는지 확인 (중복 예약 방지용)
    boolean existsByExperienceIdAndReservedStartTime(Long experienceId, LocalDateTime startTime);

    List<Reservation> findAllByStatus(ReservationStatus status);

}
