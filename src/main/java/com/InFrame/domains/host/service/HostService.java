package com.InFrame.domains.host.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import com.InFrame.common.service.BusinessValidationService;
import com.InFrame.common.service.S3UploadService;
import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.experience.entity.enums.DetailField;
import com.InFrame.domains.experience.repository.ExperienceRepository;
import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.host.repository.HostRepository;
import com.InFrame.domains.host.reqdto.HostRequestDto;
import com.InFrame.domains.host.resdto.HostMapResponseDto;
import com.InFrame.domains.host.resdto.MyHostInfoResponseDto;
import com.InFrame.domains.review.repository.ReviewRepository;
import com.InFrame.domains.user.entity.Role;
import com.InFrame.domains.user.entity.User;
import com.InFrame.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
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
    private final ExperienceRepository experienceRepository;

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
        List<Host> hostsWithExperiences = experienceRepository.findDistinctHostsWithExperiences();

        // 2. 호스트 목록으로 DTO 생성
        return hostsWithExperiences.stream()
                .map(host -> {
                    long reviewCount = reviewRepository.countByReservation_Experience_Host(host);

                    DetailField detailField = experienceRepository.findTopByHost(host)
                            .get()
                            .getDetailField();
                    return HostMapResponseDto.from(host, reviewCount, detailField);
                })
                .collect(Collectors.toList());
    }

    // 호스트 자신 정보 조회
    @Transactional(readOnly = true)
    public MyHostInfoResponseDto getMyHostInfo(User user) {
        // 1. 호스트 권한 확인
        if (user.getRole() != Role.HOST) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }
        // 2. 호스트 정보 가져오기
        Host host = user.getHost();
        if (host == null) {
            throw new CustomException(ErrorCode.HOST_NOT_FOUND);
        }
        return MyHostInfoResponseDto.from(host);
    }
}
