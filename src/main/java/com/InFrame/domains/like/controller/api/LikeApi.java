package com.InFrame.domains.like.controller.api;

import com.InFrame.domains.experience.resdto.ExperienceLikeResponseDto;
import com.InFrame.domains.host.resdto.HostLikeResponseDto;
import com.InFrame.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Like API", description = "하트(체험, 호스트) 관련 API")
public interface LikeApi {

    @Operation(summary = "체험 '하트' 토글", description = "특정 체험에 대해 '하트'를 누르거나 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "'하트' 성공 또는 취소 성공",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자 또는 체험을 찾을 수 없음", content = @Content)
    })
    ResponseEntity<?> toggleExperienceLike(
            @Parameter(description = "하트를 누를 체험의 ID", required = true)
            @PathVariable Long experienceId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "호스트 '하트' 토글", description = "특정 호스트에 대해 '하트'를 누르거나 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "'하트' 성공 또는 취소 성공",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자 또는 호스트를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<?> toggleHostLike(
            @Parameter(description = "하트를 누를 호스트의 ID", required = true)
            @PathVariable Long hostId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "내가 '하트' 누른 체험 목록 조회", description = "현재 인증된 사용자가 '하트'를 누른 모든 체험 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ExperienceLikeResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<?> getMyLikedExperiences(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "내가 '하트' 누른 호스트 목록 조회", description = "현재 인증된 사용자가 '하트'를 누른 모든 호스트 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HostLikeResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<?> getMyLikedHosts(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );
}