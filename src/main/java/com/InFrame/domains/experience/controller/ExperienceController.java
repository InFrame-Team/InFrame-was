package com.InFrame.domains.experience.controller;

import com.InFrame.domains.experience.controller.api.ExperienceApi;
import com.InFrame.domains.experience.reqdto.ExperienceRequestDto;
import com.InFrame.domains.experience.resdto.ExperienceResponseDto;
import com.InFrame.domains.experience.service.ExperienceService;
import com.InFrame.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
            @Valid
            @RequestBody ExperienceRequestDto requestDto
    ) {
        ExperienceResponseDto response = experienceService.createExperience(
                userDetails.getUser(), requestDto
        );
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping(value = "/{experienceId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadExperienceImages(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long experienceId,
            @RequestPart("images") List<MultipartFile> images
    ) {
        ExperienceResponseDto response = experienceService.uploadExperienceImages(
                userDetails.getUser(), experienceId, images
        );
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/recommend")
    public ResponseEntity<?> recommendExperiences(
            @RequestParam("query") String query,
            @RequestParam(value = "topK", defaultValue = "5") int topK
    ) {
        List<ExperienceResponseDto> recommendations = experienceService.recommendExperiences(query, topK);
        return ResponseEntity.ok(recommendations);
    }
}
