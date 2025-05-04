package com.ekip.yakupmelih.library_manage_system.repository;

import com.ekip.yakupmelih.library_manage_system.model.Odunc;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.model.Kitap;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OduncRepository extends JpaRepository<Odunc, Integer> {
    List<Odunc> findByUye(Uye uye);

    List<Odunc> findByAktifTrue();

    List<Odunc> findByDurum(Odunc.OduncDurum durum);

    List<Odunc> findByKitap(Kitap kitap);

    List<Odunc> findBySonTeslimTarihiBefore(LocalDate tarih);
}
