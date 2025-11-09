package com.InFrame.domains.experience.controller;

import com.InFrame.domains.experience.controller.api.ExperienceApi;
import com.InFrame.domains.experience.reqdto.ExperienceRequestDto;
import com.InFrame.domains.experience.resdto.ExperienceResponseDto;
import com.InFrame.domains.experience.service.ExperienceService;
import com.InFrame.security.userdetails.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Override
    @GetMapping("/recommend")
    public ResponseEntity<List<ExperienceResponseDto>> recommendExperiences(
            @RequestParam("query") String query,
            @RequestParam(value = "topK", defaultValue = "5") int topK
    ) {
        List<ExperienceResponseDto> recommendations = experienceService.recommendExperiences(query, topK);
        return ResponseEntity.ok(recommendations);
    }
}
