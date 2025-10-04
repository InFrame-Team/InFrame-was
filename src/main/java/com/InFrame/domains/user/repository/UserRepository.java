package com.InFrame.domains.user.repository;

import com.InFrame.domains.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
