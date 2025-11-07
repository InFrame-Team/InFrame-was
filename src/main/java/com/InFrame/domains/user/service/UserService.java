package com.InFrame.domains.user.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import com.InFrame.common.service.S3UploadService;
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

    // 회원정보 조회
    @Transactional(readOnly = true)
    public UserInfoResponseDto info(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserInfoResponseDto.from(user);
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
