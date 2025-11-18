package com.InFrame.domains.experience.resdto;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.host.entity.Host;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Schema(description = "체험 정보 응답 DTO")
public record ExperienceResponseDto (
        Long experienceId,
        Long hostId,
        String title,
        String description,
        int price,
        int durationInHours,
        int maxCapacityPerSlot,
        List<String> imageUrls,
        Set<DayOfWeek> availableDaysOfWeek,
        Set<LocalTime> availableTimes,
        String caution

) {
    public static  ExperienceResponseDto from(Experience experience, Host host) {
        return  new ExperienceResponseDto(
                experience.getId(),
                host.getId(),
                experience.getTitle(),
                experience.getDescription(),
                experience.getPrice(),
                experience.getDurationInHours(),
                experience.getMaxCapacityPerSlot(),
                experience.getImageUrls(),
                experience.getAvailableDaysOfWeek(),
                experience.getAvailableTimes(),
                experience.getCaution()
        );
    }
}
