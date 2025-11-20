package com.InFrame.domains.user.entity;

import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.like.entity.ExperienceLike;
import com.InFrame.domains.like.entity.HostLike;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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

    @Column
    private String profileImageUrl;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Host host;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExperienceLike> experienceLikes = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HostLike> hostLikes = new HashSet<>();

    public void updateRole(Role role) {
        this.role = role;
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Builder
    public User(String email, String password, String nickname,
                String name, Role role, String provider, String providerId,
                String profileImageUrl) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.profileImageUrl = profileImageUrl;
    }

}
