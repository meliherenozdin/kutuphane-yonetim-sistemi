package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Ceza;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.model.Odunc;
import com.ekip.yakupmelih.library_manage_system.repository.CezaRepository;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CezaService {

    private final CezaRepository cezaRepository;
    private final UyeRepository uyeRepository;

    public CezaService(CezaRepository cezaRepository, UyeRepository uyeRepository) {
        this.cezaRepository = cezaRepository;
        this.uyeRepository = uyeRepository;
    }

    public List<Ceza> tumCezalariGetir() {
        return cezaRepository.findAll();
    }

    public List<Ceza> uyeCezalariGetir(int uyeId) {
        Uye uye = uyeRepository.findById(uyeId).orElseThrow();
        return cezaRepository.findByUye(uye);
    }

    public List<Ceza> aktifCezalariGetir() {
        return cezaRepository.findByAktifTrue();
    }

    public List<Ceza> oduncBazliCezalariGetir(Odunc odunc) {
        return cezaRepository.findByOdunc(odunc);
    }

    public List<Ceza> tarihAraliginaGoreCezalariGetir(LocalDate start, LocalDate end) {
        return cezaRepository.findByCezaTarihBetween(start, end);
    }

    public Optional<Ceza> cezaGetir(int id) {
        return cezaRepository.findById(id);
    }

    public List<Ceza> findByUyeId(int uyeId) {
        return cezaRepository.findByUye_UyeID(uyeId);
    }

    @Transactional
    public Ceza cezaKaydet(Ceza ceza) {
        return cezaRepository.save(ceza);
    }

    @Transactional
    public Ceza cezaGuncelle(int id, Ceza yeniCeza) {
        return cezaRepository.findById(id)
                .map(c -> {
                    c.setOdunc(yeniCeza.getOdunc());
                    c.setUye(yeniCeza.getUye());
                    c.setCezaMiktar(yeniCeza.getCezaMiktar());
                    c.setCezaTarih(yeniCeza.getCezaTarih());
                    c.setAciklama(yeniCeza.getAciklama());
                    c.setAktif(yeniCeza.isAktif());
                    return cezaRepository.save(c);
                })
                .orElseThrow();
    }

    @Transactional
    public void cezaSil(int id) {
        cezaRepository.findById(id).ifPresent(c -> {
            c.setAktif(false);
            cezaRepository.save(c);
        });
    }
}
