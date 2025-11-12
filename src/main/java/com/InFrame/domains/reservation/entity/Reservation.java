package com.InFrame.domains.reservation.entity;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.reservation.entity.enums.ReservationStatus;
import com.InFrame.domains.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime reservedStartTime; // 예약된 시작 시간

    @Column(nullable = false)
    private int numAdults; // 예약한 성인 수

    @Column(nullable = false)
    private int numChildren; // 예약한 아동 수

    @Schema(description = "총 예약 인원")
    int totalParticipants;

    @Column(nullable = false)
    private int totalPrice; // 최종 결제(예약) 금액

    @Column(nullable = false)
    private LocalDateTime createdAt; // 예약 생성 시간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status; // 예약 상태

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Experience experience;

    @Builder
    public Reservation(LocalDateTime reservedStartTime, int numChildren, int numAdults,
                       int totalPrice, User user, Experience experience) {
        this.reservedStartTime = reservedStartTime;
        this.numAdults = numAdults;
        this.numChildren = numChildren;
        this.totalPrice = totalPrice;
        this.createdAt = LocalDateTime.now();
        this.status = ReservationStatus.RESERVED;
        this.user = user;
        this.experience = experience;
    }

    public void updateStatus(ReservationStatus status) {
        this.status = status;
    }
}
