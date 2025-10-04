package com.InFrame.domains.user.controller.api;

import com.InFrame.domains.user.reqdto.SignInRequestDto;
import com.InFrame.domains.user.reqdto.SignUpRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Tag(name = "Auth API", description = "인증 관련 API")
public interface AuthApi {

    @Operation(summary = "회원가입", description = "서비스를 이용하기 위한 회원가입입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "입력 누락 및 형식 비일치",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "필드 누락", value = """
                                    {
                                        "<field>" : "<field>는 필수 입력입니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "409", description = "중복으로 인한 회원가입 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "이메일 중복", value = """
                                    {
                                        "status" : 409,
                                        "message" : "해당 이메일은 이미 존재합니다."
                                    }
                                    """),
                            @ExampleObject(name = "닉네임 중복", value = """
                                    {
                                        "status" : 409,
                                        "message" : "해당 닉네임은 이미 존재합니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> signUp(
            @Parameter(description = "회원가입 정보")
            @RequestBody SignUpRequestDto signupRequestDto
    );

    @Operation(summary = "로그인", description = "서비스를 이용하기 위한 로그인입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "accessToken" : "<accessToken>",
                                        "userId" : "<userId>",
                                        "email" : "<email>",
                                        "role": "USER | HOST"
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "이메일이 존재하지 않음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "해당 유저를 찾을 수 없습니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "400", description = "입력 누락 및 형식 비일치",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "필드 누락", value = """
                                    {
                                        "<field>" : "<field>는 필수 입력입니다."
                                    }
                                    """),
                            @ExampleObject(name = "비밀번호 불일치", value = """
                                    {
                                        "status" : 400,
                                        "message" : "비밀번호가 일치하지 않습니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> signIn(
            @Parameter(description = "로그인 정보")
            @RequestBody SignInRequestDto signInRequestDto
    );

    @Operation(summary = "소셜 로그인", description = "서비스를 이용하기 위한 소셜 로그인입니다.")
    void socialLogin(
            @Parameter(description = "naver 혹은 kakao 입력")
            @PathVariable String provider, HttpServletResponse response
    ) throws IOException;


    @Operation(summary = "로그아웃", description = "로그아웃을 하는 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    ResponseEntity<?> signOut();

    @Operation(summary = "이메일 중복 확인", description = "사용 가능한 이메일인지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 이메일"),
            @ApiResponse(responseCode = "409", description = "이미 사용 중인 이메일",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                {
                                    "status" : 409,
                                    "message" : "해당 이메일은 이미 존재합니다."
                                }
                                """)
                    }))
    })
    ResponseEntity<?> checkEmail(
            @Parameter(description = "확인할 이메일", required = true, example = "likelion13@gmail.com")
            @RequestParam String email
    );


    @Operation(summary = "닉네임 중복 확인", description = "사용 가능한 닉네임인지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 닉네임"),
            @ApiResponse(responseCode = "409", description = "이미 사용 중인 닉네임",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                {
                                    "status" : 409,
                                    "message" : "해당 닉네임은 이미 존재합니다."
                                }
                                """)
                    }))
    })
    ResponseEntity<?> checkNickname(
            @Parameter(description = "확인할 닉네임", required = true, example = "코코넛")
            @RequestParam String nickname
    );

}
