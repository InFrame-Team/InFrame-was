package com.InFrame.domains.experience.entity;

import com.InFrame.domains.experience.entity.enums.DetailField;
import com.InFrame.domains.experience.entity.enums.ProfessionalField;
import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.like.entity.ExperienceLike;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfessionalField professionalField; // 전문분야

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DetailField detailField; // 상세분야

    @Column(columnDefinition = "TEXT")
    private String certifications; // 보유한 자격증

    @Column(columnDefinition = "TEXT")
    private String companyInfo; // 업체 정보

    @Column(nullable = false)
    private String title; // 체험 제목

    @Column(columnDefinition = "TEXT")
    private String description; // 체험 설명

    @Column(nullable = false)
    private int price; // 체험 기본 가격

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "experience_images", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "image_url", columnDefinition = "TEXT")
    private List<String> imageUrls = new ArrayList<>(); // 체험 관련 이미지

    @Column(nullable = false)
    private int durationInHours; // 체험  소요 시간 (시간 단위)

    @Column(nullable = false)
    private int maxCapacityPerSlot; // 각 시간 당 예약 가능한 최대 인원

    // 예약 가능한 요일 목록
    @ElementCollection(targetClass = DayOfWeek.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "experience_available_days", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "day_of_week", nullable = false)
    private Set<DayOfWeek> availableDaysOfWeek = new HashSet<>();

    // 예약 가능한 시간 목록
    @ElementCollection(targetClass = LocalTime.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "experience_available_times", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "available_time", nullable = false)
    private Set<LocalTime> availableTimes = new HashSet<>();

    // 프로그램 유의사항
    @Column(columnDefinition = "TEXT")
    private String caution; // 체험 유의사항

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Host host;

    @OneToMany(mappedBy = "experience", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExperienceLike> likes = new HashSet<>();

    @Builder
    public Experience(ProfessionalField professionalField,
                      DetailField detailField, String title, String description,
                      String certifications, String companyInfo, String caution,
                      int price, int durationInHours, int maxCapacityPerSlot,
                      Set<DayOfWeek> availableDaysOfWeek, Set<LocalTime> availableTimes,
                      Host host) {
        this.professionalField = professionalField;
        this.detailField = detailField;
        this.certifications = certifications;
        this.companyInfo = companyInfo;
        this.title = title;
        this.description = description;
        this.caution = caution;
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

    public void updateImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
