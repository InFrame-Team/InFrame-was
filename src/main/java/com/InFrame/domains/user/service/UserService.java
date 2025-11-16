package com.InFrame.domains.user.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import com.InFrame.common.service.S3UploadService;
import com.InFrame.domains.like.repository.ExperienceLikeRepository;
import com.InFrame.domains.like.repository.HostLikeRepository;
import com.InFrame.domains.reservation.repository.ReservationRepository;
import com.InFrame.domains.review.repository.ReviewRepository;
import com.InFrame.domains.user.entity.Role;
import com.InFrame.domains.user.entity.User;
import com.InFrame.domains.user.repository.UserRepository;
import com.InFrame.domains.user.resdto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final S3UploadService s3UploadService;
    private final ReservationRepository reservationRepository;
    private final HostLikeRepository hostLikeRepository;
    private final ExperienceLikeRepository experienceLikeRepository;
    private final ReviewRepository reviewRepository;

    // 프로필 이미지 등록 및 수정
    @Transactional
    public String uploadProfileImage(User user, MultipartFile file) {
        // 1. 기존 이미지가 있다면 S3에서 삭제
        String oldImageUrl = user.getProfileImageUrl();
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            s3UploadService.deleteFile(oldImageUrl);
        }

        // 2. 새 이미지 S3에 업로드
        String newImageUrl = s3UploadService.uploadFile(file, "profile");

        // 3. 유저 정보에 새 이미지 URL 업데이트
        user.updateProfileImage(newImageUrl);
        userRepository.save(user);

        return newImageUrl;
    }

    // 마이페이지 정보 조회
    @Transactional(readOnly = true)
    public UserInfoResponseDto getMyPageInfo(User user) {
        long reservationCount = reservationRepository.countByUser(user);
        long hostLikeCount = hostLikeRepository.countByUser(user);
        long experienceLikeCount = experienceLikeRepository.countByUser(user);
        long reviewCount = reviewRepository.countByUser(user);

        return UserInfoResponseDto.from(
                user,
                reservationCount,
                hostLikeCount,
                experienceLikeCount,
                reviewCount
        );
    }

    // 유저 역할 변경 (호스트 <-> 유저)
    @Transactional
    public void updateUserRole(User user, Role newRole) {
        // 호스트로 변경 시, 기존에 호스트 정보가 등록된 사람인지 확인
        if (newRole == Role.HOST) {
            if (user.getHost() == null) {
                throw new CustomException(ErrorCode.HOST_INFO_NOT_FOUND);
            }
        }

        user.updateRole(newRole); //
        userRepository.save(user);
    }

    // 유저 삭제
    @Transactional
    public void deleteUser(User user) {
        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
            s3UploadService.deleteFile(user.getProfileImageUrl());
        }
        userRepository.delete(user);
    }
}
