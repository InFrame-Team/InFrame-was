package com.InFrame.domains.host.controller;

import com.InFrame.domains.host.controller.api.HostApi;
import com.InFrame.domains.host.reqdto.HostRequestDto;
import com.InFrame.domains.host.service.HostService;
import com.InFrame.security.userdetails.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/host")
public class HostController implements HostApi {
    private final HostService hostService;

    @Override
    @PostMapping("/update")
    public ResponseEntity<?> changeToHost(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid HostRequestDto hostRequestDto) {
        hostService.changeToHost(userDetails.getUser(), hostRequestDto);
        return ResponseEntity.ok().build();
    }
}
