package com.InFrame.domains.experience.repository;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.host.entity.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    // 호스트가 등록한 모든 체험 찾기
    List<Experience> findAllByHost(Host host);

    // 호스트의 체험 중 첫 번째 데이터를 가져옴
    Optional<Experience> findTopByHost(Host host);

    // 체험을 가진 호스트 조회
    @Query("SELECT DISTINCT e.host FROM Experience e")
    List<Host> findDistinctHostsWithExperiences();

    @Query("SELECT DISTINCT e FROM Experience e " +
            "LEFT JOIN FETCH e.imageUrls " +
            "WHERE e.host IN :hosts")
    List<Experience> findAllByHostInWithImages(@Param("hosts") List<Host> hosts);
}
