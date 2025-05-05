package com.ekip.yakupmelih.library_manage_system.repository;

import com.ekip.yakupmelih.library_manage_system.model.Ceza;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.model.Odunc;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CezaRepository extends JpaRepository<Ceza, Integer> {
    List<Ceza> findByUye(Uye uye);

    List<Ceza> findByAktifTrue();

    List<Ceza> findByOdunc(Odunc odunc);

    List<Ceza> findByCezaTarihBetween(LocalDate start, LocalDate end);

    List<Ceza> findByUye_UyeID(int uyeID);
}
