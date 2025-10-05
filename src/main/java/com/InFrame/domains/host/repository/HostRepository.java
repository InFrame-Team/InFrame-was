package com.InFrame.domains.host.repository;

import com.InFrame.domains.host.entity.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<Host, Long> {
    boolean existsByBusinessNumber(String businessNumber);
}
