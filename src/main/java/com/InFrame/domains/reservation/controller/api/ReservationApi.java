package com.InFrame.domains.reservation.controller.api;

import com.InFrame.domains.reservation.reqdto.ReservationRequestDto;
import com.InFrame.domains.reservation.resdto.HostReservationResponseDto;
import com.InFrame.domains.reservation.resdto.MyReservationResponseDto;
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

import java.time.LocalDate;

@Tag(name = "Reservation API", description = "예약 관련 API")
public interface ReservationApi {

    @Operation(summary = "예약 가능 시간 목록 조회",
            description = "특정 날짜의 예약 가능한 시간 목록을 조회하는 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    [
                                        {
                                            "startTime": "09:00:00",
                                            "isAvailable": false
                                        },
                                        {
                                            "startTime": "11:00:00",
                                            "isAvailable": true
                                        },
                                        {
                                            "startTime": "14:00:00",
                                            "isAvailable": true
                                        }
                                    ]
                                    """)
                    })),
            @ApiResponse(responseCode = "401", description = "액세스 토큰 미입력/만료",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status" : 401,
                                        "message" : "토큰이 없거나 만료되었습니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "체험을 찾을 수 없음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status" : 404,
                                        "message" : "해당 체험을 찾을 수 없습니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> getAvailableSlots(
            @Parameter(description = "체험 ID", required = true)
            @PathVariable Long experienceId,

            @Parameter(description = "조회할 날짜 (YYYY-MM-DD)", required = true, example = "2025-10-30")
            @RequestParam LocalDate date
    );


    @Operation(summary = "체험 예약 생성", description = "사용자가 체험을 예약하는 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "예약 생성 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "reservationId": 1,
                                        "experienceId": 1,
                                        "userId": 1,
                                        "reservedStartTime": "2025-11-10T09:00:00",
                                        "calculatedEndTime": "2025-11-10T12:00:00",
                                        "numAdults": 2,
                                        "numChildren": 0,
                                        "totalParticipants": 2,
                                        "totalPrice": 100000,
                                        "createdAt": "2025-10-30T19:35:00"
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "400", description = "잘못된 예약 요청 (입력값 누락, 유효성 검증 실패, 휴무일, 시간 오류, 인원 초과 등)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "필드 누락", value = """
                                    {
                                        "startTime": "시작 시간은 필수입니다."
                                    }
                                    """),
                            @ExampleObject(name = "유효성 검증 실패", value = """
                                    {
                                        "reservationDate": "과거 날짜는 예약할 수 없습니다."
                                    }
                                    """),
                            @ExampleObject(name = "휴무일", value = """
                                    {
                                        "status": 400,
                                        "message": "체험 휴무일에는 예약할 수 없습니다."
                                    }
                                    """),
                            @ExampleObject(name = "인원 0명", value = """
                                    {
                                        "status": 400,
                                        "message": "예약 인원은 1명 이상이어야 합니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "401", description = "액세스 토큰 미입력/만료",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status" : 401,
                                        "message" : "토큰이 없거나 만료되었습니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "체험을 찾을 수 없음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status" : 404,
                                        "message" : "해당 체험을 찾을 수 없습니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "409", description = "예약 슬롯 중복",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status" : 409,
                                        "message" : "이미 예약이 마감된 시간입니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> createReservation(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails,

            @Parameter(description = "예약 생성 정보")
            @RequestBody @Valid ReservationRequestDto requestDto
    );

    @Operation(summary = "내 예약 내역 조회", description = "로그인한 사용자의 모든 예약 내역을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MyReservationResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    ResponseEntity<?> getMyReservations(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "예약 취소", description = "로그인한 사용자가 자신의 예약을 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 취소 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "예약 취소 권한 없음 (본인 예약 아님)"),
            @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "예약 취소가 불가능한 상태 (예: 이미 완료됨)")
    })
    ResponseEntity<Void> cancelReservation(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails,

            @Parameter(description = "취소할 예약 ID", required = true)
            @PathVariable Long reservationId
    );

    @Operation(summary = "호스트 체험 예약 목록 조회", description = "호스트가 등록한 체험들에 대해 들어온 예약 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HostReservationResponseDto.class))))
    @ApiResponse(responseCode = "403", description = "호스트 권한 없음")
    ResponseEntity<?> getHostReservations(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );
}