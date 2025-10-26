package com.InFrame.domains.user.controller;

import com.InFrame.domains.user.controller.api.UserApi;
import com.InFrame.domains.user.resdto.UserInfoResponseDto;
import com.InFrame.domains.user.service.UserService;
import com.InFrame.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserInfoResponseDto  userInfo = UserInfoResponseDto.from(userDetails.getUser());
        return ResponseEntity.ok(userInfo);
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponseDto> getUserInfoById(@PathVariable Long userId) {
        UserInfoResponseDto userInfo = userService.info(userId);
        return ResponseEntity.ok(userInfo);
    }

    @Override
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyAccount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(userDetails.getUser());
        return ResponseEntity.ok().build();
    }

}
