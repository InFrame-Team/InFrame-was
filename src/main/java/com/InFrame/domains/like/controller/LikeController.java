package com.InFrame.domains.like.controller;

import com.InFrame.domains.experience.resdto.ExperienceLikeResponseDto;
import com.InFrame.domains.host.resdto.HostLikeResponseDto;
import com.InFrame.domains.like.controller.api.LikeApi;
import com.InFrame.domains.like.service.LikeService;
import com.InFrame.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController implements LikeApi {
    private final LikeService likeService;

    @Override
    @PostMapping("/experience/{experienceId}")
    public ResponseEntity<String> toggleExperienceLike(
            @PathVariable Long experienceId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boolean liked = likeService.toggleExperienceLike(userDetails.getUser().getId(), experienceId);
        String message = liked ? "체험을 좋아합니다." : "체험 좋아요를 취소합니다.";
        return ResponseEntity.ok(message);
    }

    @Override
    @PostMapping("/host/{hostId}")
    public ResponseEntity<String> toggleHostLike(
            @PathVariable Long hostId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boolean liked = likeService.toggleHostLike(userDetails.getUser().getId(), hostId);
        String message = liked ? "호스트를 좋아합니다." : "호스트 좋아요를 취소합니다.";
        return ResponseEntity.ok(message);
    }

    @Override
    @GetMapping("/experience")
    public ResponseEntity<?> getMyLikedExperiences(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<ExperienceLikeResponseDto> likedExperiences =
                likeService.getMyLikedExperiences(userDetails.getUser().getId());
        return ResponseEntity.ok(likedExperiences);
    }

    @Override
    @GetMapping("/host")
    public ResponseEntity<?> getMyLikedHosts(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<HostLikeResponseDto> likedHosts =
                likeService.getMyLikedHosts(userDetails.getUser().getId());
        return ResponseEntity.ok(likedHosts);
    }
}
