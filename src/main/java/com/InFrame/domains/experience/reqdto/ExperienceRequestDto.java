package com.InFrame.domains.experience.reqdto;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.host.entity.Host;

public record ExperienceRequestDto (
        String title,
        String description,
        int price,
        int time
){
    public Experience toEntity(Host host) {
        return Experience.builder()
                .host(host)
                .title(title)
                .description(description)
                .price(price)
                .time(time)
                .build();
    }
}
