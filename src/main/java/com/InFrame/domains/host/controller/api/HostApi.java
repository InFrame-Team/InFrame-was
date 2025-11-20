package com.InFrame.domains.host.controller.api;

import com.InFrame.domains.host.reqdto.HostRequestDto;
import com.InFrame.domains.host.resdto.HostDetailResponseDto;
import com.InFrame.domains.host.resdto.HostMapResponseDto;
import com.InFrame.domains.host.resdto.MyHostInfoResponseDto;
import com.InFrame.domains.host.resdto.TopHostResponseDto;
import com.InFrame.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Host API", description = "호스트 관련 API")
public interface HostApi {

    @Operation(summary = "호스트로 변경", description = "호스트 역할 변경을 하기 위한 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "호스트로 변경 성공"),
            @ApiResponse(responseCode = "400", description = "입력 누락 및 형식 비일치",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "필드 누락", value = """
                                    {
                                        "<field>" : "<field>는 필수 입력입니다."
                                    }
                                    """),
                            @ExampleObject(name = "이메일 형식 비일치", value = """
                                    {
                                        "email": "이메일 형식을 맞춰주세요."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "409", description = "중복으로 인한 변경 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "이미 호스트인 회원", value = """
                                    {
                                        "status" : 409,
                                        "message" : "이미 호스트로 등록된 유저입니다."
                                    }
                                    """),
                            @ExampleObject(name = "이미 사용중인 사업자 번호", value = """
                                    {
                                        "status" : 409,
                                        "message" : "이미 등록된 사업자 번호입니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> changeToHost(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid HostRequestDto hostRequestDto
    );

    @Operation(summary = "사업자 번호 검증", description = "DB 중복 확인 및 공공데이터 API를 통해 유효한 사업자 번호인지 검증합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "확인되었습니다. (사용 가능한 번호)"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 사업자 번호 (형식 오류, 휴/폐업 등)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "형식 오류", value = "{\"status\": 400, \"message\": \"사업자 번호 형식이 올바르지 않습니다.\"}"),
                            @ExampleObject(name = "미등록", value = "{\"status\": 400, \"message\": \"국세청에 등록되지 않은 사업자 번호입니다.\"}"),
                            @ExampleObject(name = "휴/폐업", value = "{\"status\": 400, \"message\": \"휴업 또는 폐업 상태의 사업자입니다.\"}")
                    })),
            @ApiResponse(responseCode = "409", description = "이미 등록된 사업자 번호입니다."),
            @ApiResponse(responseCode = "500", description = "API 조회 실패")
    })
    ResponseEntity<?> checkBusinessNumber(
            @Parameter(description = "검증할 사업자 번호", required = true, example = "1234567890")
            @RequestParam String businessNumber
    );

    @Operation(summary = "업체 로고 이미지 업로드", description = "호스트의 업체 로고 이미지를 업로드(수정)합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "companyLogoUrl": "https://s3.../new-logo-url.jpg"
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 등)"),
            @ApiResponse(responseCode = "403", description = "호스트 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "호스트 정보를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "파일 업로드 실패")
    })
    ResponseEntity<?> uploadMyCompanyLogo(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails,

            @Parameter(description = "업로드할 로고 이미지 파일 (form-data key: 'file')", required = true)
            @RequestParam("file") MultipartFile file
    );

    @Operation(summary = "전체 호스트 목록 조회 (지도용)", description = "지도에 표시하기 위한 모든 호스트의 간략한 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HostMapResponseDto.class))))
    })
    ResponseEntity<?> getAllHostsForMap();

    @Operation(summary = "내 호스트 정보 조회 (마이페이지)", description = "내 호스트 정보와 통계(좋아요, 리뷰, 예약 건수)를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MyHostInfoResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "호스트 정보를 찾을 수 없음")
    })
    ResponseEntity<?> getMyHostInfo(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "후기 많은 호스트 Top 5 조회",
            description = "후기 개수를 기준으로 Top 5 호스트를 조회합니다. (평균 평점 포함)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TopHostResponseDto.class))))
    })
    ResponseEntity<?> getTop5Hosts();

    @Operation(summary = "특정 호스트 상세 정보 조회", description = "호스트 ID를 기준으로 상세 정보, 평점 및 후기 수를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HostDetailResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "호스트 정보를 찾을 수 없음")
    })
    ResponseEntity<?> getHostDetail(
            @Parameter(description = "조회할 호스트 ID", required = true)
            @PathVariable Long hostId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails // ✨ NEW PARAMETER
    );
}
