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
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // User 와 1:1

    @Builder
    public Host(String businessName, String businessPhoneNumber,
                String businessEmail, String kakaoAddress,
                String businessNumber, User user) {
        this.businessNumber = businessNumber;
        this.businessName = businessName;
        this.businessPhoneNumber = businessPhoneNumber;
        this.businessEmail = businessEmail;
        this.kakaoAddress = kakaoAddress;
        this.user = user;
    }
}
