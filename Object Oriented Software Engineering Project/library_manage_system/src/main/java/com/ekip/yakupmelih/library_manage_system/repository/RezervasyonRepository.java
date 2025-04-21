package com.ekip.yakupmelih.library_manage_system.repository;

import java.util.List;
import com.ekip.yakupmelih.library_manage_system.model.Rezervasyon;
import com.ekip.yakupmelih.library_manage_system.model.Uye;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RezervasyonRepository extends JpaRepository<Rezervasyon, Integer> {
    List<Rezervasyon> findByUye(Uye uye);

    List<Rezervasyon> findByDurum(String durum);
}