package com.InFrame.domains.host.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
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

    public void changeToHost(User user, HostRequestDto hostRequestDto) {
        if (user.getRole() == Role.HOST) {
            throw new CustomException(ErrorCode.USER_ALREADY_HOST);
        }

        if (hostRepository.existsByBusinessNumber(hostRequestDto.businessNumber())) {
            throw new CustomException(ErrorCode.BUSINESS_NUMBER_ALREADY_EXISTS);
        }

        // =======================================================================
        // TODO: 공공데이터 포털 API 복구 시 로직 구현
        // =======================================================================

        user.updateRole(Role.HOST);
        userRepository.save(user);

        Host host = hostRequestDto.toEntity(user);
        hostRepository.save(host);
    }
}
