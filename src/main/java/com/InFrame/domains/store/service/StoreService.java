package com.InFrame.domains.store.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.store.entity.Store;
import com.InFrame.domains.store.repository.StoreRepository;
import com.InFrame.domains.store.reqdto.StoreRequestDto;
import com.InFrame.domains.user.entity.Role;
import com.InFrame.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    // 호스트의 가게 등록
    @Transactional
    public Store createLocation(User user, StoreRequestDto requestDto) {
        if (user.getRole() != Role.HOST) { //
            throw new CustomException(ErrorCode.USER_NOT_HOST);
        }

        Host host = user.getHost();

        Store location = requestDto.toEntity(host);

        return storeRepository.save(location);
    }
}
