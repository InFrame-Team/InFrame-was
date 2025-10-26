package com.InFrame.domains.experience.controller.api;

import com.InFrame.domains.experience.reqdto.ExperienceRequestDto;
import com.InFrame.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Experience API", description = "체험 관련 API")
public interface ExperienceApi {

    @Operation(summary = "체험 생성", description = "호스트가 새로운 체험을 생성하는 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "experienceId": 1,
                                        "hostId": 12,
                                        "title": "한 입의 예술, 핸드메이드 초콜릿",
                                        "description": "저희 체험은 100년 째 이어지고 있는 머시기 ... (생략)",
                                        "price": 50000,
                                        "time" : 3
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
            @ApiResponse(responseCode = "403", description = "호스트 권한 없음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status" : 403,
                                        "message" : "해당 유저는 호스트가 아닙니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> createExperience(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails,

            @Parameter(description = "생성할 체험 정보")
            @RequestBody @Valid ExperienceRequestDto requestDto);

}
