package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Ceza;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.CezaRepository;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CezaService {

    private final CezaRepository cezaRepository;
    private final UyeRepository uyeRepository;

    public CezaService(CezaRepository cezaRepository, UyeRepository uyeRepository) {
        this.cezaRepository = cezaRepository;
        this.uyeRepository = uyeRepository;
    }

    public List<Ceza> getCezaByUyeId(int uyeId) {
        Uye uye = uyeRepository.findById(uyeId).orElseThrow();
        return cezaRepository.findByUye(uye);
    }
}
