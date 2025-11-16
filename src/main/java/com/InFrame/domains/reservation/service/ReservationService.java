package com.InFrame.domains.reservation.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.experience.repository.ExperienceRepository;
import com.InFrame.domains.reservation.entity.Reservation;
import com.InFrame.domains.reservation.repository.ReservationRepository;
import com.InFrame.domains.reservation.reqdto.ReservationRequestDto;
import com.InFrame.domains.reservation.resdto.AvailableSlotDto;
import com.InFrame.domains.reservation.resdto.MyReservationResponseDto;
import com.InFrame.domains.reservation.resdto.ReservationResponseDto;
import com.InFrame.domains.review.entity.Review;
import com.InFrame.domains.review.repository.ReviewRepository;
import com.InFrame.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ExperienceRepository experienceRepository;
    private final ReviewRepository reviewRepository;

    // 특정 날짜의 예약 가능한 시간 목록 조회
    @Transactional(readOnly = true)
    public List<AvailableSlotDto> getAvailableSlots(Long experienceId, LocalDate date) {
        // 1. 체험 정보 조회
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new CustomException(ErrorCode.EXPERIENCE_NOT_FOUND));

        // 2. 해당 날짜가 예약 가능 요일인지 확인
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (!experience.getAvailableDaysOfWeek().contains(dayOfWeek)) {
            return List.of();
        }

        // 3. 해당 날짜에 이미 예약된 시작 시간 목록 조회
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        Set<LocalTime> reservedTimes = reservationRepository.findReservedStartTimesByExperienceIdAndDate(
                        experienceId, startOfDay, endOfDay)
                .stream()
                .map(LocalDateTime::toLocalTime)
                .collect(Collectors.toSet());

        // 4. 호스트가 설정한 전체 시간 목록과 이미 예약된 시간 목록을 비교하여 DTO 생성
        return experience.getAvailableTimes().stream() //
                .map(availableTime -> {
                    boolean isAlreadyReserved = reservedTimes.contains(availableTime);
                    return new AvailableSlotDto(availableTime, !isAlreadyReserved);
                })
                .sorted((s1, s2) -> s1.startTime().compareTo(s2.startTime()))
                .collect(Collectors.toList());
    }

    // 사용자가 체험을 예약
    @Transactional
    public ReservationResponseDto createReservation(User user, ReservationRequestDto requestDto) {
        // 1. 체험 정보 조회
        Experience experience = experienceRepository.findById(requestDto.experienceId())
                .orElseThrow(() -> new CustomException(ErrorCode.EXPERIENCE_NOT_FOUND));

        // 2. 예약 요청 유효성 검증
        validateReservationRequest(experience, requestDto);

        // 3. 중복 예약 확인 (DB 쿼리)
        LocalDateTime requestedStartTime = LocalDateTime.of(requestDto.reservationDate(), requestDto.startTime());

        if (reservationRepository.existsByExperienceIdAndReservedStartTime(requestDto.experienceId(), requestedStartTime)) {
            throw new CustomException(ErrorCode.RESERVATION_SLOT_NOT_AVAILABLE);
        }

        // 4. 예약 정보 계산
        int totalParticipants = requestDto.numAdults() + requestDto.numChildren();
        if (totalParticipants <= 0) {
            throw new CustomException(ErrorCode.INVALID_PARTICIPANT_COUNT);
        }

        int totalPrice = experience.getPrice() * totalParticipants;

        // 5. Reservation 엔티티 생성
        Reservation reservation = Reservation.builder()
                .user(user)
                .experience(experience)
                .reservedStartTime(requestedStartTime)
                .numAdults(requestDto.numAdults())
                .numChildren(requestDto.numChildren())
                .totalPrice(totalPrice)
                .build();

        // 6. 저장 및 DTO 변환 후 반환
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponseDto.from(savedReservation);
    }

    // 내 예약 내역 조회
    @Transactional(readOnly = true)
    public List<MyReservationResponseDto> getMyReservations(User user) {
        List<Reservation> reservations = reservationRepository.findAllByUserOrderByReservedStartTimeDesc(user);

        // 이 예약 목록에 대해 이미 작성된 리뷰가 있는지 한 번에 조회
        List<Review> reviews = reviewRepository.findAllByReservationIn(reservations);

        Set<Long> reviewedReservationIds = reviews.stream()
                .map(review -> review.getReservation().getId())
                .collect(Collectors.toSet());

        // DTO로 변환
        return reservations.stream()
                .map(reservation -> {
                    boolean reviewWritten = reviewedReservationIds.contains(reservation.getId());
                    return MyReservationResponseDto.from(reservation, reviewWritten);
                })
                .collect(Collectors.toList());
    }

    // 예약 요청 유효성 검증
    private void validateReservationRequest(Experience experience, ReservationRequestDto requestDto) {
        // 예약 가능 요일 확인
        DayOfWeek dayOfWeek = requestDto.reservationDate().getDayOfWeek();
        if (!experience.getAvailableDaysOfWeek().contains(dayOfWeek)) {
            throw new CustomException(ErrorCode.RESERVATION_ON_CLOSED_DAY);
        }

        // 예약 가능 시간 확인
        if (!experience.getAvailableTimes().contains(requestDto.startTime())) {
            throw new CustomException(ErrorCode.INVALID_RESERVATION_TIME);
        }

        // 최대 인원 수 확인
        if ((requestDto.numAdults() + requestDto.numChildren()) > experience.getMaxCapacityPerSlot()) {
            throw new CustomException(ErrorCode.RESERVATION_CAPACITY_EXCEEDED);
        }
    }

}
