package com.ekip.yakupmelih.library_manage_system.repository;

import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KitapRepository extends JpaRepository<Kitap, Integer> {
    Optional<Kitap> findByIsbn(String isbn);
}
