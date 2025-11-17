package com.InFrame.domains.reservation.controller;

import com.InFrame.domains.reservation.controller.api.ReservationApi;
import com.InFrame.domains.reservation.reqdto.ReservationRequestDto;
import com.InFrame.domains.reservation.resdto.AvailableSlotDto;
import com.InFrame.domains.reservation.resdto.MyReservationResponseDto;
import com.InFrame.domains.reservation.resdto.ReservationResponseDto;
import com.InFrame.domains.reservation.service.ReservationService;
import com.InFrame.security.userdetails.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReservationController implements ReservationApi {
    private final ReservationService reservationService;

    @Override
    @GetMapping("/experiences/{experienceId}/available-slots")
    public ResponseEntity<?> getAvailableSlots(
            @PathVariable Long experienceId,
            @RequestParam LocalDate date) {

        List<AvailableSlotDto> slots = reservationService.getAvailableSlots(experienceId, date);

        return ResponseEntity.ok(slots);
    }

    @Override
    @PostMapping("/reservation")
    public ResponseEntity<?> createReservation(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ReservationRequestDto requestDto) {

        ReservationResponseDto responseDto = reservationService.createReservation(userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Override
    @GetMapping("/reservation/me")
    public ResponseEntity<?> getMyReservations(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<MyReservationResponseDto> reservations = reservationService.getMyReservations(userDetails.getUser());
        return ResponseEntity.ok(reservations);
    }

    @Override
    @PatchMapping("/reservation/{reservationId}/cancel")
    public ResponseEntity<Void> cancelReservation(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reservationId) {

        reservationService.cancelReservation(userDetails.getUser(), reservationId);

        return ResponseEntity.ok().build();
    }
}
