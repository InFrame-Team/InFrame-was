package com.InFrame.domains.reservation.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.experience.repository.ExperienceRepository;
import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.like.repository.HostLikeRepository;
import com.InFrame.domains.reservation.entity.Reservation;
import com.InFrame.domains.reservation.entity.enums.ReservationStatus;
import com.InFrame.domains.reservation.repository.ReservationRepository;
import com.InFrame.domains.reservation.reqdto.ReservationRequestDto;
import com.InFrame.domains.reservation.resdto.AvailableSlotDto;
import com.InFrame.domains.reservation.resdto.HostReservationResponseDto;
import com.InFrame.domains.reservation.resdto.MyReservationResponseDto;
import com.InFrame.domains.reservation.resdto.ReservationResponseDto;
import com.InFrame.domains.review.entity.Review;
import com.InFrame.domains.review.repository.ReviewRepository;
import com.InFrame.domains.user.entity.Role;
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
    private final HostLikeRepository hostLikeRepository;

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

        if (reservationRepository.existsValidReservationAtTime(requestDto.experienceId(), requestedStartTime)) {
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
        // 1. 사용자 예약 목록 조회
        List<Reservation> reservations = reservationRepository.findAllByUserOrderByReservedStartTimeDesc(user);

        // 예약 목록이 비어있으면 빈 리스트 반환
        if (reservations.isEmpty()) {
            return List.of();
        }

        // 2-1. 리뷰 상태 bulk 조회 (사용자 제공 코드)
        List<Review> reviews = reviewRepository.findAllByReservationIn(reservations);

        Set<Long> reviewedReservationIds = reviews.stream()
                .map(review -> review.getReservation().getId())
                .collect(Collectors.toSet());

        // 3. DTO로 변환 (리뷰 상태 및 호스트 좋아요 상태 포함)
        return reservations.stream()
                .map(reservation -> {
                    boolean reviewWritten = reviewedReservationIds.contains(reservation.getId());

                    // 호스트 좋아요 상태 확인
                    // 각 예약의 체험 -> 호스트를 가져와서 사용자의 좋아요 상태를 확인합니다.
                    Host host = reservation.getExperience().getHost();
                    boolean isHostLiked = hostLikeRepository.existsByUserAndHost(user, host);

                    return MyReservationResponseDto.from(reservation, reviewWritten, isHostLiked);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelReservation(User user, Long reservationId) {
        // 1. 예약 정보 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        // 2. 예약 취소 권한 확인
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 3. 예약 상태 확인
        if (reservation.getStatus() != ReservationStatus.RESERVED) {
            throw new CustomException(ErrorCode.RESERVATION_NOT_CANCELLABLE);
        }

        // 4. 예약 상태 변경
        reservation.updateStatus(ReservationStatus.CANCELLED);
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

    // 호스트가 만든 체험에 예약된 리스트 조회
    public List<HostReservationResponseDto> getHostReservations(User user) {
        if (user.getRole() != Role.HOST) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        Host host = user.getHost();
        if (host == null) {
            throw new CustomException(ErrorCode.HOST_NOT_FOUND);
        }

        List<Reservation> reservations = reservationRepository.findAllByHostIdWithExperience(host.getId());

        return reservations.stream()
                .map(HostReservationResponseDto::from)
                .collect(Collectors.toList());
    }

}
