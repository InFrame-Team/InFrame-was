package com.InFrame.domains.store.controller;

import com.InFrame.domains.store.reqdto.StoreRequestDto;
import com.InFrame.domains.store.service.StoreService;
import com.InFrame.security.userdetails.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/location")
public class StoreController {
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<?> createLocation(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid StoreRequestDto requestDto) {

        storeService.createLocation(userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
