package com.InFrame.domains.store.reqdto;

import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.store.entity.Store;

public record StoreRequestDto(
        String address,
        Double latitude,
        Double longitude
) {
    public Store toEntity(Host host) {
        return Store.builder()
                .host(host)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
