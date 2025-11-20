package com.InFrame.domains.experience.controller.api;

import com.InFrame.domains.experience.reqdto.ExperienceRequestDto;
import com.InFrame.domains.experience.resdto.ExperienceDetailResponseDto;
import com.InFrame.domains.experience.resdto.ExperienceImagesResponseDto;
import com.InFrame.domains.experience.resdto.ExperienceResponseDto;
import com.InFrame.domains.experience.resdto.ExperienceSummaryResponseDto;
import com.InFrame.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Experience API", description = "체험 관련 API")
public interface ExperienceApi {

    @Operation(summary = "체험 생성", description = "새로운 체험을 텍스트 정보로 먼저 등록합니다. (이미지 제외)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "체험 생성 성공",
                    content = @Content(schema = @Schema(implementation = ExperienceResponseDto.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createExperience(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails,

            @Valid
            @RequestBody
            @Parameter(description = "체험 정보 DTO")
            ExperienceRequestDto requestDto
    );


    @Operation(summary = "체험 이미지 업로드", description = "생성된 체험 ID에 이미지 파일들을 업로드합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 업로드 성공",
                    content = @Content(schema = @Schema(implementation = ExperienceResponseDto.class)))
    })
    ResponseEntity<?> uploadExperienceImages(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails,

            @Parameter(description = "이미지를 업로드할 체험 ID")
            @PathVariable Long experienceId,

            @RequestPart("images")
            @Parameter(
                    description = "체험 이미지 파일 목록",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            array = @ArraySchema(
                                    schema = @Schema(type = "string", format = "binary")
                            )
                    )
            )
            List<MultipartFile> images
    );

    @Operation(summary = "체험 상세 조회", description = "체험 ID로 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = ExperienceDetailResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 체험을 찾을 수 없음",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{experienceId}")
    ResponseEntity<?> getExperienceDetail(
            @Parameter(description = "조회할 체험 ID", required = true)
            @PathVariable Long experienceId
    );

    @Operation(summary = "AI 체험 추천", description = "검색 쿼리와 유사한 체험을 AI가 추천합니다.")
    @ApiResponse(responseCode = "200", description = "추천 목록 조회 성공")
    ResponseEntity<?> recommendExperiences(
            @Parameter(name = "query", in = ParameterIn.QUERY, required = true, description = "검색어 (예: 친구와 둘이 시험 끝나고 감성 있게 즐기기 좋은 체험)")
            @RequestParam("query") String query,

            @Parameter(name = "topK", in = ParameterIn.QUERY, description = "받아올 추천 개수 (기본 5개)")
            @RequestParam(value = "topK", defaultValue = "5") int topK
    );

    @Operation(summary = "호스트의 모든 체험 목록 조회", description = "호스트가 등록한 모든 체험의 요약 정보를 조회합니다. (인증 필요)")
    @ApiResponse(responseCode = "200", description = "체험 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExperienceSummaryResponseDto.class))))
    @ApiResponse(responseCode = "403", description = "권한 없음 (호스트가 아닌 사용자)",
            content = @Content(schema = @Schema(hidden = true)))
    ResponseEntity<?> getExperiencesByHost(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "특정 호스트의 모든 체험 목록 조회", description = "Host ID를 기준으로 해당 호스트가 등록한 모든 체험의 요약 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "체험 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExperienceSummaryResponseDto.class))))
    @ApiResponse(responseCode = "404", description = "호스트를 찾을 수 없음")
    ResponseEntity<?> getExperiencesByHostId(
            @Parameter(description = "조회할 호스트 ID", required = true)
            @PathVariable Long hostId
    );

    @Operation(summary = "특정 호스트의 체험 이미지 목록 조회", description = "Host ID를 기준으로 해당 호스트가 등록한 모든 체험의 이미지 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "이미지 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExperienceImagesResponseDto.class))))
    @ApiResponse(responseCode = "404", description = "호스트를 찾을 수 없음")
    ResponseEntity<?> getExperienceImagesByHostId(
            @Parameter(description = "조회할 호스트 ID", required = true)
            @PathVariable Long hostId
    );
}
