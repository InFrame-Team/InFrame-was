package com.InFrame.domains.experience.resdto;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.host.entity.Host;

public record ExperienceResponseDto (
        Long experienceId,
        Long hostId,
        String title,
        String description,
        int price,
        int time
) {
    public static  ExperienceResponseDto from(Experience experience, Host host) {
        return  new ExperienceResponseDto(
                experience.getId(),
                host.getId(),
                experience.getTitle(),
                experience.getDescription(),
                experience.getPrice(),
                experience.getTime()
        );
    }
}
