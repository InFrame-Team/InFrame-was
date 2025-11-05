package com.InFrame.domains.experience.controller.api;

import com.InFrame.domains.experience.resdto.EnumResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Enum", description = "Enum-based 필드 옵션 조회 API")
public interface EnumApi {

    @Operation(summary = "분야 카테고리 Enum 목록 조회", description = "체험 등록에 필요한 '분야 카테고리' Enum 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EnumResponseDto.class))))
    })
    @GetMapping("/categories")
    ResponseEntity<?> getCategory();

    @Operation(summary = "상세분야 Enum 목록 조회", description = "체험 등록에 필요한 '상세분야' Enum 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상세분야 목록 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EnumResponseDto.class))))
    })
    @GetMapping("/detailFields")
    ResponseEntity<?> getDetailField();

    @Operation(summary = "전문분야 Enum 목록 조회", description = "체험 등록에 필요한 '전문분야' Enum 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전문분야 목록 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EnumResponseDto.class))))
    })
    @GetMapping("/professionalFields")
    ResponseEntity<?> getProfessionalField();
}
