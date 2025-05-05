package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Raf;
import com.ekip.yakupmelih.library_manage_system.repository.RafRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RafService {

    private final RafRepository rafRepository;

    public RafService(RafRepository rafRepository) {
        this.rafRepository = rafRepository;
    }

    public List<Raf> tumRaflariGetir() {
        return rafRepository.findAll();
    }

    public List<Raf> aktifRaflariGetir() {
        return rafRepository.findByAktifTrue();
    }

    public List<Raf> bolumeGoreRaflariGetir(String bolum) {
        return rafRepository.findByBolumContainingIgnoreCase(bolum);
    }

    public Optional<Raf> rafBulByRafNo(String rafNo) {
        return rafRepository.findById(rafNo);
    }

    @Transactional
    public Raf rafEkle(Raf raf) {
        return rafRepository.save(raf);
    }

    @Transactional
    public List<Raf> topluRafEkle(List<Raf> raflar) {
        return rafRepository.saveAll(raflar);
    }

    @Transactional
    public Raf rafGuncelle(String rafNo, Raf yeniRaf) {
        return rafRepository.findById(rafNo)
                .map(r -> {
                    r.setBolum(yeniRaf.getBolum());
                    r.setAciklama(yeniRaf.getAciklama());
                    r.setAktif(yeniRaf.isAktif());
                    return rafRepository.save(r);
                })
                .orElseThrow(() -> new RuntimeException("Raf bulunamadı: " + rafNo));
    }

    @Transactional
    public void rafSil(String rafNo) {
        rafRepository.findById(rafNo).ifPresent(r -> {
            r.setAktif(false);
            rafRepository.save(r);
        });
    }

    @Transactional
    public void rafKaliciOlarakSil(String rafNo) {
        rafRepository.deleteById(rafNo);
    }
}