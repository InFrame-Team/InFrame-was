package com.InFrame.domains.host.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import com.InFrame.common.service.BusinessValidationService;
import com.InFrame.common.service.S3UploadService;
import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.experience.entity.enums.DetailField;
import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.host.repository.HostRepository;
import com.InFrame.domains.host.reqdto.HostRequestDto;
import com.InFrame.domains.host.resdto.HostDetailResponseDto;
import com.InFrame.domains.host.resdto.HostMapResponseDto;
import com.InFrame.domains.host.resdto.MyHostInfoResponseDto;
import com.InFrame.domains.host.resdto.TopHostResponseDto;
import com.InFrame.domains.like.repository.HostLikeRepository;
import com.InFrame.domains.reservation.entity.enums.ReservationStatus;
import com.InFrame.domains.reservation.repository.ReservationRepository;
import com.InFrame.domains.review.repository.ReviewRepository;
import com.InFrame.domains.user.entity.Role;
import com.InFrame.domains.user.entity.User;
import com.InFrame.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class HostService {
    private final HostRepository hostRepository;
    private final UserRepository userRepository;
    private final BusinessValidationService businessValidationService;
    private final S3UploadService s3UploadService;
    private final ReviewRepository reviewRepository;
    private final HostLikeRepository hostLikeRepository;
    private final ReservationRepository reservationRepository;

    // 호스트로 변경
    public void changeToHost(User user, HostRequestDto hostRequestDto) {
        if (user.getRole() == Role.HOST) {
            throw new CustomException(ErrorCode.USER_ALREADY_HOST);
        }

        validateBusinessNumber(hostRequestDto.businessNumber());

        user.updateRole(Role.HOST);
        userRepository.save(user);

        Host host = hostRequestDto.toEntity(user);
        hostRepository.save(host);
    }

    // 사업자 번호 검증
    @Transactional(readOnly = true)
    public void validateBusinessNumber(String businessNumber) {
        // 1. DB에서 중복 확인
        if (hostRepository.existsByBusinessNumber(businessNumber)) {
            throw new CustomException(ErrorCode.BUSINESS_NUMBER_ALREADY_EXISTS);
        }

        // 2. 공공데이터 API로 유효성 및 상태 확인
        businessValidationService.validateBusinessNumber(businessNumber);
    }

    // 업체 로고 이미지 업로드
    @Transactional
    public String uploadCompanyLogo(User user, MultipartFile file) {
        // 1. 호스트 정보 가져오기
        Host host = user.getHost();

        // 2. 기존 로고 이미지가 있다면 S3에서 삭제
        String oldLogoUrl = host.getCompanyLogoUrl();
        if (oldLogoUrl != null && !oldLogoUrl.isEmpty()) {
            s3UploadService.deleteFile(oldLogoUrl);
        }

        // 3. 새 로고 이미지 S3에 업로드
        String newLogoUrl = s3UploadService.uploadFile(file, "logo");

        // 4. 호스트 정보에 새 이미지 URL 업데이트
        host.updateCompanyLogo(newLogoUrl);
        hostRepository.save(host);

        return newLogoUrl;
    }

    // 지도 표시용 전체 호스트 목록 조회
    @Transactional(readOnly = true)
    public List<HostMapResponseDto> getAllHostsForMap() {
        // 1. 체험이 있는 호스트만 조회
        List<Host> hostsWithExperiences = hostRepository.findAllWithExperiencesForMap();

        // 2. 호스트 목록으로 DTO 생성
        return hostsWithExperiences.stream()
                .map(host -> {
                    long reviewCount = reviewRepository.countByReservation_Experience_Host(host);

                    DetailField detailField = host.getExperiences().stream()
                            .findFirst()
                            .get()
                            .getDetailField();

                    // 3. 최저가 계산 로직
                    OptionalInt minPriceOpt = host.getExperiences().stream()
                            .mapToInt(Experience::getPrice)
                            .min();

                    Integer lowestPrice = minPriceOpt.getAsInt();

                    return HostMapResponseDto.from(host, reviewCount, detailField, lowestPrice);
                })
                .collect(Collectors.toList());
    }

    // 호스트 마이페이지 정보 조회
    @Transactional(readOnly = true)
    public MyHostInfoResponseDto getMyHostInfo(User user) {
        Host host = hostRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.HOST_INFO_NOT_FOUND));

        // 1. 받은 좋아요 수
        long likeCount = hostLikeRepository.countByHost(host);

        // 2. 받은 리뷰 수
        long reviewCount = reviewRepository.countByReservation_Experience_Host(host);

        // 3. 예약 완료 수
        long confirmedCount = reservationRepository.countByExperience_HostAndStatus(host, ReservationStatus.RESERVED);

        // 4. 체험 완료 수
        long completedCount = reservationRepository.countByExperience_HostAndStatus(host, ReservationStatus.COMPLETED);

        return MyHostInfoResponseDto.from(host, user, likeCount, reviewCount, confirmedCount, completedCount);
    }

    // 후기 많은 호스트 Top 5 조회
    public List<TopHostResponseDto> getTop5HostsByReviews() {

        Pageable pageable = PageRequest.of(0, 5);
        List<Object[]> topHostData = reviewRepository.findTopHostDataByReviewCount(pageable);

        // Host ID 추출
        List<Long> hostIds = topHostData.stream()
                .map(obj -> (Long) obj[0])
                .collect(Collectors.toList());

        // Host 엔티티 조회 후 Map으로 변환
        Map<Long, Host> hostMap = hostRepository.findAllById(hostIds)
                .stream()
                .collect(Collectors.toMap(Host::getId, h -> h));

        // DTO 변환
        return topHostData.stream()
                .map(obj -> {
                    Long hostId = (Long) obj[0];
                    Long reviewCount = (Long) obj[1];
                    Double averageRating = (Double) obj[2];

                    Host host = hostMap.get(hostId);

                    return TopHostResponseDto.from(
                            host,
                            reviewCount != null ? reviewCount : 0L,
                            averageRating != null ? averageRating : 0.0
                    );
                })
                .collect(Collectors.toList());
    }

    // 특정 호스트 상세 정보 조회
    @Transactional(readOnly = true)
    public HostDetailResponseDto getHostDetail(Long hostId) {
        // 1. Host와 User 정보 조회 (Eager fetch)
        Host host = hostRepository.findByIdWithUser(hostId)
                .orElseThrow(() -> new CustomException(ErrorCode.HOST_INFO_NOT_FOUND));

        User user = host.getUser();

        // 2. 리뷰 통계 정보 조회
        Object[] reviewSummary = reviewRepository.findReviewSummaryByHostId(hostId);

        Long reviewCount = (Long) reviewSummary[0];
        Double averageRating = (Double) reviewSummary[1];

        // 3. DTO로 매핑
        return HostDetailResponseDto.from(
                user,
                host,
                reviewCount != null ? reviewCount : 0L,
                averageRating != null ? averageRating : 0.0
        );
    }

}
