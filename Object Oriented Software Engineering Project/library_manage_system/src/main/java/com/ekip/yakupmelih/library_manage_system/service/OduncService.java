package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Odunc;
import com.ekip.yakupmelih.library_manage_system.repository.OduncRepository;

import java.util.List;

public class OduncService {

    private final OduncRepository oduncRepository;

    public OduncService(OduncRepository oduncRepository) {
        this.oduncRepository = oduncRepository;
    }

    public List<Odunc> getOduncByUyeId(int uyeId) {
        return oduncRepository.findAll()
                .stream()
                .filter(odunc -> odunc.getUye().getUyeID() == uyeId)
                .toList();
    }
}
