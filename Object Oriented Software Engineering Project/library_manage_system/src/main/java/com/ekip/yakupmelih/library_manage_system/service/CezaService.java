package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Ceza;
import com.ekip.yakupmelih.library_manage_system.repository.CezaRepository;

import java.util.List;

public class CezaService {

    private final CezaRepository cezaRepository;

    public CezaService(CezaRepository cezaRepository) {
        this.cezaRepository = cezaRepository;
    }

    public List<Ceza> getCezaByUyeId(int uyeId) {
        return cezaRepository.findAll()
                .stream()
                .filter(ceza -> ceza.getUye().getUyeID() == uyeId)
                .toList();
    }
}
