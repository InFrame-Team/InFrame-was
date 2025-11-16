package com.InFrame.domains.user.controller;

import com.InFrame.domains.user.controller.api.UserApi;
import com.InFrame.domains.user.entity.Role;
import com.InFrame.domains.user.resdto.UserInfoResponseDto;
import com.InFrame.domains.user.service.UserService;
import com.InFrame.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserInfoResponseDto myPageInfo = userService.getMyPageInfo(userDetails.getUser());
        return ResponseEntity.ok(myPageInfo);
    }

    @Override
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyAccount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/me/role")
    public ResponseEntity<Void> updateMyRole(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("newRole") Role newRole) {

        userService.updateUserRole(userDetails.getUser(), newRole);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping(value = "/me/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMyProfileImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("file") MultipartFile file) {

        String imageUrl = userService.uploadProfileImage(userDetails.getUser(), file);

        return ResponseEntity.ok(Map.of("profileImageUrl", imageUrl));
    }
}
