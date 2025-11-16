package com.InFrame.domains.host.controller;

import com.InFrame.domains.host.controller.api.HostApi;
import com.InFrame.domains.host.reqdto.HostRequestDto;
import com.InFrame.domains.host.resdto.HostMapResponseDto;
import com.InFrame.domains.host.resdto.MyHostInfoResponseDto;
import com.InFrame.domains.host.service.HostService;
import com.InFrame.security.userdetails.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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

    @Override
    @GetMapping("/check-business-number")
    public ResponseEntity<?> checkBusinessNumber(@RequestParam String businessNumber) {
        hostService.validateBusinessNumber(businessNumber);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping(value = "/companyLogo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMyCompanyLogo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("file") MultipartFile file) {

        String logoUrl = hostService.uploadCompanyLogo(userDetails.getUser(), file);

        return ResponseEntity.ok(Map.of("companyLogoUrl", logoUrl));
    }

    @Override
    @GetMapping("/map")
    public ResponseEntity<?> getAllHostsForMap() {
        List<HostMapResponseDto> hostList = hostService.getAllHostsForMap();
        return ResponseEntity.ok(hostList);
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity<?> getMyHostInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MyHostInfoResponseDto myHostInfo = hostService.getMyHostInfo(userDetails.getUser());
        return ResponseEntity.ok(myHostInfo);
    }
}
