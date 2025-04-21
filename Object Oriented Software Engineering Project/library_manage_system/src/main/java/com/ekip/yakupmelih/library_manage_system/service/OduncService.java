package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Odunc;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.OduncRepository;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class OduncService {

    private final OduncRepository oduncRepository;
    private final UyeRepository uyeRepository;

    public OduncService(OduncRepository oduncRepository, UyeRepository uyeRepository) {
        this.oduncRepository = oduncRepository;
        this.uyeRepository = uyeRepository;
    }

    public List<Odunc> getOduncByUyeId(int uyeId) {
        Uye uye = uyeRepository.findById(uyeId).orElseThrow();
        return oduncRepository.findByUye(uye);
    }
}
