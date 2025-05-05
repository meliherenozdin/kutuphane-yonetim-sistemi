package com.ekip.yakupmelih.library_manage_system.repository;

import com.ekip.yakupmelih.library_manage_system.model.Yazar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YazarRepository extends JpaRepository<Yazar, Integer> {
    List<Yazar> findByYazarAdiContaining(String yazarAdi);
}
