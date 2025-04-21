package com.ekip.yakupmelih.library_manage_system.repository;

import com.ekip.yakupmelih.library_manage_system.model.Uye;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UyeRepository extends JpaRepository<Uye, Integer> {
    Optional<Uye> findByEmail(String email);

    Optional<Uye> findByTelNo(String telNo);

    boolean existsByEmail(String email);

}
