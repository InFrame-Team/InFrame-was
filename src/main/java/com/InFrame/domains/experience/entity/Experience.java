package com.InFrame.domains.experience.entity;

import com.InFrame.domains.host.entity.Host;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 체험 제목

    @Column(columnDefinition = "TEXT")
    private String description; // 체험 설명

    @Column(nullable = false)
    private int price; // 체험 기본 가격

    @Column(nullable = false)
    private int durationInHours; // 체험  소요 시간 (시간 단위)

    @Column(nullable = false)
    private int maxCapacityPerSlot; // 각 시간 당 예약 가능한 최대 인원

    // 예약 가능한 요일 목록
    @ElementCollection(targetClass = DayOfWeek.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "experience_available_days", joinColumns = @JoinColumn(name = "experience_id")) // 테이블명 변경
    @Column(name = "day_of_week", nullable = false)
    private Set<DayOfWeek> availableDaysOfWeek = new HashSet<>();

    // 예약 가능한 시간 목록
    @ElementCollection(targetClass = LocalTime.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "experience_available_times", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "available_time", nullable = false)
    private Set<LocalTime> availableTimes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Host host;

    @Builder
    public Experience(String title, String description, int price,
                      int durationInHours, int maxCapacityPerSlot,
                      Set<DayOfWeek> availableDaysOfWeek, Set<LocalTime> availableTimes,
                      Host host) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.durationInHours = durationInHours;
        this.maxCapacityPerSlot = maxCapacityPerSlot;
        if (availableDaysOfWeek != null) {
            this.availableDaysOfWeek = availableDaysOfWeek;
        }
        if (availableTimes != null) {
            this.availableTimes = availableTimes;
        }
        this.host = host;
    }
}
