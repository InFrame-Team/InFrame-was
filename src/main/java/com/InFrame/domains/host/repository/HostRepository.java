package com.InFrame.domains.host.repository;

import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HostRepository extends JpaRepository<Host, Long> {
    boolean existsByBusinessNumber(String businessNumber);

    Optional<Host> findByUser(User user);

    @Query("SELECT DISTINCT h FROM Host h JOIN FETCH h.experiences")
    List<Host> findAllWithExperiencesForMap();

    @Query("SELECT h FROM Host h JOIN FETCH h.user WHERE h.id = :hostId")
    Optional<Host> findByIdWithUser(@Param("hostId") Long hostId);
}
