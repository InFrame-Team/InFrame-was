package com.InFrame.domains.reservation.service;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.reservation.entity.Reservation;
import com.InFrame.domains.reservation.entity.enums.ReservationStatus;
import com.InFrame.domains.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationScheduler {
    private final ReservationRepository reservationRepository;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void updateReservationStatus() {
        log.info("예약 상태 업데이트 작업 시작");
        LocalDateTime now = LocalDateTime.now();

        // 1. 'RESERVED' 상태인 예약만 조회
        List<Reservation> candidates = reservationRepository.findAllByStatus(ReservationStatus.RESERVED);

        // 2. 체험 종료 시간이 지났는지 필터링
        List<Reservation> completedReservations = candidates.stream()
                .filter(reservation -> {
                    Experience experience = reservation.getExperience();
                    if (experience == null) return false;

                    // 종료 시간 = 예약된 시작 시간 + 체험 소요 시간
                    LocalDateTime endTime = reservation.getReservedStartTime()
                            .plusHours(experience.getDurationInHours());

                    // 종료 시간이 현재 시간보다 과거인지 확인
                    return endTime.isBefore(now);
                })
                .peek(reservation -> reservation.updateStatus(ReservationStatus.COMPLETED)) // 상태 변경
                .toList();

        // 3. 완료된 예약들 일괄 저장
        if (!completedReservations.isEmpty()) {
            reservationRepository.saveAll(completedReservations);
            log.info("{}건의 예약을 'COMPLETED' 상태로 변경했습니다.", completedReservations.size());
        }
    }
}
