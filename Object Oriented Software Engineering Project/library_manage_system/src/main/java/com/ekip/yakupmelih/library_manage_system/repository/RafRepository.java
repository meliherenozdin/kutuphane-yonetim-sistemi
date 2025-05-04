package com.ekip.yakupmelih.library_manage_system.repository;

import com.ekip.yakupmelih.library_manage_system.model.Raf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RafRepository extends JpaRepository<Raf, String> {
    List<Raf> findByAktifTrue();

    List<Raf> findByBolumContainingIgnoreCase(String bolum);
}