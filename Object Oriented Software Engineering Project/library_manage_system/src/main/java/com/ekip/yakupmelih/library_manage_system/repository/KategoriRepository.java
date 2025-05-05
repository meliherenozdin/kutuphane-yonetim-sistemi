package com.ekip.yakupmelih.library_manage_system.repository;

import com.ekip.yakupmelih.library_manage_system.model.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KategoriRepository extends JpaRepository<Kategori, Integer> {
    List<Kategori> findByTuruContaining(String turu);
}
