package com.InFrame.domains.store.repository;

import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // 호스트가 등록한 모든 가게 찾기
    List<Store> findAllByHost(Host host);
}
