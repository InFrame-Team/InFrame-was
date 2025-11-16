package com.InFrame.domains.like.repository;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.like.entity.ExperienceLike;
import com.InFrame.domains.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExperienceLikeRepository extends JpaRepository<ExperienceLike, Long> {

    // 유저와 체험 객체로 좋아요 찾기
    Optional<ExperienceLike> findByUserAndExperience(User user, Experience experience);

    // 특정 유저가 누른 모든 '하트'를 '체험' 정보와 함께 조회
    @Query("SELECT DISTINCT el FROM ExperienceLike el " +
            "JOIN FETCH el.experience ex " +
            "LEFT JOIN FETCH ex.imageUrls " +
            "JOIN FETCH ex.host h " +
            "JOIN FETCH h.user " +
            "WHERE el.user = :user")
    List<ExperienceLike> findAllByUserWithExperienceAndHost(@Param("user") User user);

    // 유저가 누른 호스트 좋아요 수
    long countByUser(User user);
}