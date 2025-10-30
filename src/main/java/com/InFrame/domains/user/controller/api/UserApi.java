package com.InFrame.domains.user.controller.api;

import com.InFrame.domains.user.resdto.UserInfoResponseDto;
import com.InFrame.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User API", description = "회원 관련 API")
public interface UserApi {

    @Operation(summary = "내 정보 조회", description = "내 정보를 조회하기 위한 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "user_id": 1,
                                        "email": "<email>",
                                        "name": "<name>",
                                        "nickname": "<nickname>",
                                        "role": "USER | BUSINESS",
                                        "profileImageUrl": "https://s3.../image-url.jpg"
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
                                    """),
                    }))
    })
    ResponseEntity<?> getMyInfo(
            @Parameter(description = "JWT 토큰기반 유저 조회")
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "유저 정보 조회", description = "특정 유저 정보를 조회하기 위한 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "user_id": 1,
                                        "email": "<email>",
                                        "name": "<name>",
                                        "nickname": "<nickname>",
                                        "role": "USER | BUSINESS"
                                        "profileImageUrl": "https://s3.../image-url.jpg"
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
                                    """),
                    }))
    })
    ResponseEntity<UserInfoResponseDto> getUserInfoById(
            @Parameter(description = "userId")
            @PathVariable Long userId
    );


    @Operation(summary = "회원탈퇴(삭제)", description = "회원탈퇴를 하는 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status" : 401,
                                        "message" : "토큰이 없거나 만료되었습니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<String> deleteMyAccount(
            @Parameter(description = "JWT 토큰기반 유저 조회")
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "프로필 이미지 업로드", description = "내 프로필 이미지를 업로드(수정)합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "profileImageUrl": "https://s3.../new-image-url.jpg"
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 등)"),
            @ApiResponse(responseCode = "500", description = "파일 업로드 실패")
    })
    ResponseEntity<?> uploadMyProfileImage(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails,

            @Parameter(description = "업로드할 이미지 파일 (form-data key: 'file')", required = true)
            @RequestParam("file") MultipartFile file
    );
}
