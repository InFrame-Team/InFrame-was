package com.InFrame.domains.experience.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.experience.repository.ExperienceRepository;
import com.InFrame.domains.experience.reqdto.ExperienceRequestDto;
import com.InFrame.domains.experience.resdto.ExperienceResponseDto;
import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.user.entity.Role;
import com.InFrame.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExperienceService {
    private final ExperienceRepository experienceRepository;

    // 체험 생성
    public ExperienceResponseDto createExperience(User user, ExperienceRequestDto requestDto) {
        if (user.getRole() != Role.HOST) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        Host host = user.getHost();

        if (host == null) {
            throw new CustomException(ErrorCode.HOST_NOT_FOUND);
        }

        Experience experience = requestDto.toEntity(host);

        Experience savedExperience = experienceRepository.save(experience);

        return  ExperienceResponseDto.from(savedExperience, host);
    }
}
