package com.InFrame.domains.host.entity;

import com.InFrame.domains.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
public class Host {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String businessNumber; // 사업자 번호

    @Column(nullable = false)
    private String businessName; // 사업자명

    @Column(nullable = false)
    private String businessPhoneNumber; // 고객센터 전화번호

    @Column
    private String businessEmail; // 고객센터 이메일

    @Column
    private String kakaoAddress; // 카카오톡 채널 주소

    @Column(length = 25)
    private String description; // 호스트 소개

    @Column(nullable = false)
    private Double latitude; // 위도

    @Column(nullable = false)
    private Double longitude; // 경도

    @Column(nullable = false)
    private String addressBase; // 기본 주소

    @Column
    private String addressDetail; // 상세 주소

    @Column
    private LocalTime contactStartTime; // 연락 가능 시작 시간

    @Column
    private LocalTime contactEndTime; // 연락 가능 종료 시간

    @Column(columnDefinition = "TEXT")
    private String cancellationPolicy; // 취소 정책

    @Column
    private String companyLogoUrl; // 업체 로고 이미지 URL

    @Column(columnDefinition = "TEXT")
    private String detailedDescription; // 자세한 소개


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // User 와 1:1

    @Builder
    public Host(String businessName, String businessPhoneNumber,
                String businessEmail, String kakaoAddress,
                String businessNumber, String description,
                Double latitude, Double longitude,
                String addressBase, String addressDetail,
                LocalTime contactStartTime, LocalTime contactEndTime,
                String cancellationPolicy, String companyLogoUrl,
                String detailedDescription, User user) {
        this.businessNumber = businessNumber;
        this.businessName = businessName;
        this.businessPhoneNumber = businessPhoneNumber;
        this.businessEmail = businessEmail;
        this.kakaoAddress = kakaoAddress;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressBase = addressBase;
        this.addressDetail = addressDetail;
        this.contactStartTime = contactStartTime;
        this.contactEndTime = contactEndTime;
        this.cancellationPolicy = cancellationPolicy;
        this.companyLogoUrl = companyLogoUrl;
        this.detailedDescription = detailedDescription;
        this.user = user;
    }

    public void updateCompanyLogo(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }
}
