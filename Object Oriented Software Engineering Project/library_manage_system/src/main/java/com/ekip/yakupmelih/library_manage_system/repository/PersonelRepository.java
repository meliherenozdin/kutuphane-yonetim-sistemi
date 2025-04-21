package com.ekip.yakupmelih.library_manage_system.repository;

import com.ekip.yakupmelih.library_manage_system.model.Personel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonelRepository extends JpaRepository<Personel, Integer> {
    Optional<Personel> findByEmail(String email);
    Optional<Personel> findByTelNo(String telNo);
}