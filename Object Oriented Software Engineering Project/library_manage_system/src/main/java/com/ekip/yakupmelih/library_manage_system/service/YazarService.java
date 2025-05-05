package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Yazar;
import com.ekip.yakupmelih.library_manage_system.repository.YazarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class YazarService {

    private final YazarRepository yazarRepository;

    public YazarService(YazarRepository yazarRepository) {
        this.yazarRepository = yazarRepository;
    }

    public List<Yazar> tumYazarlariGetir() {
        return yazarRepository.findAll();
    }

    public List<Yazar> aktifYazarlariGetir() {
        return yazarRepository.findByAktifTrue();
    }

    public List<Yazar> adaGoreYazarlariGetir(String yazarAdi) {
        return yazarRepository.findByYazarAdiContainingIgnoreCase(yazarAdi);
    }

    public Optional<Yazar> yazarBulById(int yazarID) {
        return yazarRepository.findById(yazarID);
    }

    @Transactional
    public Yazar yazarEkle(Yazar yazar) {
        return yazarRepository.save(yazar);
    }

    @Transactional
    public List<Yazar> topluYazarEkle(List<Yazar> yazarlar) {
        return yazarRepository.saveAll(yazarlar);
    }

    @Transactional
    public Yazar yazarGuncelle(int yazarID, Yazar yeniYazar) {
        return yazarRepository.findById(yazarID)
                .map(y -> {
                    y.setYazarAdi(yeniYazar.getYazarAdi());
                    y.setYazarSoyad(yeniYazar.getYazarSoyad());
                    y.setAciklama(yeniYazar.getAciklama());
                    y.setAktif(yeniYazar.isAktif());
                    return yazarRepository.save(y);
                })
                .orElseThrow(() -> new RuntimeException("Yazar bulunamadı: " + yazarID));
    }

    @Transactional
    public void yazarSil(int yazarID) {
        yazarRepository.findById(yazarID).ifPresent(y -> {
            y.setAktif(false);
            yazarRepository.save(y);
        });
    }

    @Transactional
    public void yazarKaliciOlarakSil(int yazarID) {
        yazarRepository.deleteById(yazarID);
    }
}