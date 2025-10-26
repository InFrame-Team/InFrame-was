package com.InFrame.domains.experience.repository;

import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.host.entity.Host;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    // 호스트가 등록한 모든 체험 찾기
    List<Experience> findAllByHost(Host host);
}
