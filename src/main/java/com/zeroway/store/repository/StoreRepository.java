package com.zeroway.store.repository;

import com.zeroway.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface StoreRepository extends JpaRepository<Store, Long> {

    Page<Store> findByAddressNewContains(String keyword, Pageable pageable);
}
