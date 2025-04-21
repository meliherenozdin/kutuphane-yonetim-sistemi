package com.ekip.yakupmelih.library_manage_system.repository;

import com.ekip.yakupmelih.library_manage_system.model.Odunc;
import com.ekip.yakupmelih.library_manage_system.model.Uye;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OduncRepository extends JpaRepository<Odunc, Integer> {
    List<Odunc> findByUye(Uye uye);
}
