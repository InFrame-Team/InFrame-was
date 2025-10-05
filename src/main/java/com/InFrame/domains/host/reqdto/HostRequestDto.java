package com.InFrame.domains.host.reqdto;

import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.user.entity.User;

public record HostRequestDto(
        String businessNumber,
        String businessName,
        String businessPhoneNumber,
        String businessEmail,
        String kakaoAddress
) {
    public Host toEntity(User user) {
        return Host.builder()
                .user(user)
                .businessNumber(businessNumber)
                .businessName(businessName)
                .businessPhoneNumber(businessPhoneNumber)
                .businessEmail(businessEmail)
                .kakaoAddress(kakaoAddress)
                .build();
    }
}
