package com.InFrame.domains.experience.controller.api;

import com.InFrame.domains.experience.reqdto.ExperienceRequestDto;
import com.InFrame.domains.experience.resdto.ExperienceResponseDto;
import com.InFrame.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Experience API", description = "체험 관련 API")
public interface ExperienceApi {

    @Operation(summary = "체험 생성", description = "호스트가 새로운 체험을 생성하는 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "체험 생성 성공",
                    content = @Content(schema = @Schema(implementation = ExperienceResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "403", description = "호스트가 아님"),
            @ApiResponse(responseCode = "404", description = "호스트 정보를 찾을 수 없음")
    })
    ResponseEntity<?> createExperience(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails,

            @Parameter(description = "체험 정보")
            @RequestPart("requestDto") ExperienceRequestDto requestDto,

            @Parameter(description = "체험 이미지 파일 목록")
            @RequestPart("images") List<MultipartFile> images);


    @Operation(summary = "AI 체험 추천", description = "검색 쿼리와 유사한 체험을 AI가 추천합니다.")
    @ApiResponse(responseCode = "200", description = "추천 목록 조회 성공")
    @GetMapping("/recommend")
    ResponseEntity<List<ExperienceResponseDto>> recommendExperiences(
            @Parameter(name = "query", in = ParameterIn.QUERY, required = true, description = "검색어 (예: 친구와 둘이 시험 끝나고 감성 있게 즐기기 좋은 체험)")
            @RequestParam("query") String query,

            @Parameter(name = "topK", in = ParameterIn.QUERY, description = "받아올 추천 개수 (기본 5개)")
            @RequestParam(value = "topK", defaultValue = "5") int topK
    );

}
