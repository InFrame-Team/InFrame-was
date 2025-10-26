package com.InFrame.domains.experience.entity;

import com.InFrame.domains.host.entity.Host;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 체험 이름

    @Column(columnDefinition = "TEXT")
    private String description; // 체험 설명

    @Column(nullable = false)
    private int price; // 체험 가격

    @Column(nullable = false)
    private int time; // 체험 시간

    @ManyToOne(fetch = FetchType.LAZY)
    private Host host;

    @Builder
    public Experience(String title, String description, int price,
                      int time, Host host) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.time = time;
    }
}
