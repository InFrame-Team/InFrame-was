package com.InFrame.domains.experience.controller;

import com.InFrame.domains.experience.controller.api.ExperienceApi;
import com.InFrame.domains.experience.reqdto.ExperienceRequestDto;
import com.InFrame.domains.experience.resdto.ExperienceResponseDto;
import com.InFrame.domains.experience.service.ExperienceService;
import com.InFrame.security.userdetails.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/host/experiences")
public class ExperienceController implements ExperienceApi {

    private final ExperienceService experienceService;

    @Override
    @PostMapping
    public ResponseEntity<?> createExperience(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ExperienceRequestDto requestDto) {

        ExperienceResponseDto responseDto = experienceService.createExperience(userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
