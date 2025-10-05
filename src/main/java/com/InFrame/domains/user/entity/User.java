package com.InFrame.domains.user.entity;

import com.InFrame.domains.host.entity.Host;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String provider;

    @Column
    private String providerId;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Host host;

    public void updateRole(Role role) {
        this.role = role;
    }

    @Builder
    public User(String email, String password, String nickname,
                String name, Role role, String provider, String providerId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}
