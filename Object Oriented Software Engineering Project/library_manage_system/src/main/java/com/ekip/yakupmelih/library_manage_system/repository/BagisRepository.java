package com.ekip.yakupmelih.library_manage_system.repository;

import com.ekip.yakupmelih.library_manage_system.model.Bagis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BagisRepository extends JpaRepository<Bagis, Integer> {
    List<Bagis> findByAktifTrue();

    List<Bagis> findByAciklamaContainingIgnoreCase(String aciklama);

    List<Bagis> findByUye_UyeID(int uyeID);
}