package com.ekip.yakupmelih.library_manage_system.repository;

import com.ekip.yakupmelih.library_manage_system.model.Ceza;
import com.ekip.yakupmelih.library_manage_system.model.Uye;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CezaRepository extends JpaRepository<Ceza, Integer> {
    List<Ceza> findByUye(Uye uye);
}
