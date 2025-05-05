package com.ekip.yakupmelih.library_manage_system.repository;

import com.ekip.yakupmelih.library_manage_system.model.Kutuphane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KutuphaneRepository extends JpaRepository<Kutuphane, String> {
    Kutuphane findByAdi(String adi);
} 