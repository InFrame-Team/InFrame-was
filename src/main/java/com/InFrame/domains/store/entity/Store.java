package com.InFrame.domains.store.entity;

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
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address; // 주소

    @Column(nullable = false)
    private double latitude; // 위도

    @Column(nullable = false)
    private double longitude; // 경도

    @ManyToOne(fetch = FetchType.LAZY)
    private Host host;

    @Builder
    public Store(String address, double latitude,
                 double longitude, Host host) {
        this.host = host;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
