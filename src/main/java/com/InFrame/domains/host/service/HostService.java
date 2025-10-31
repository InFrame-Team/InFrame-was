package com.InFrame.domains.host.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import com.InFrame.common.service.BusinessValidationService;
import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.host.repository.HostRepository;
import com.InFrame.domains.host.reqdto.HostRequestDto;
import com.InFrame.domains.user.entity.Role;
import com.InFrame.domains.user.entity.User;
import com.InFrame.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class HostService {
    private final HostRepository hostRepository;
    private final UserRepository userRepository;
    private final BusinessValidationService businessValidationService;

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
}
