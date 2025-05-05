package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Kategori;
import com.ekip.yakupmelih.library_manage_system.repository.KategoriRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class KategoriService {

    private final KategoriRepository kategoriRepository;

    public KategoriService(KategoriRepository kategoriRepository) {
        this.kategoriRepository = kategoriRepository;
    }

    public List<Kategori> tumKategorileriGetir() {
        return kategoriRepository.findAll();
    }

    public List<Kategori> aktifKategorileriGetir() {
        return kategoriRepository.findByAktifTrue();
    }

    public List<Kategori> aciklamayaGoreKategorileriGetir(String aciklama) {
        return kategoriRepository.findByAciklamaContainingIgnoreCase(aciklama);
    }

    public Optional<Kategori> kategoriBulById(int kategoriID) {
        return kategoriRepository.findById(kategoriID);
    }

    @Transactional
    public Kategori kategoriEkle(Kategori kategori) {
        return kategoriRepository.save(kategori);
    }

    @Transactional
    public List<Kategori> topluKategoriEkle(List<Kategori> kategoriler) {
        return kategoriRepository.saveAll(kategoriler);
    }

    @Transactional
    public Kategori kategoriGuncelle(int kategoriID, Kategori yeniKategori) {
        return kategoriRepository.findById(kategoriID)
                .map(k -> {
                    k.setTuru(yeniKategori.getTuru());
                    k.setAciklama(yeniKategori.getAciklama());
                    k.setAktif(yeniKategori.isAktif());
                    return kategoriRepository.save(k);
                })
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı: " + kategoriID));
    }

    @Transactional
    public void kategoriSil(int kategoriID) {
        kategoriRepository.findById(kategoriID).ifPresent(k -> {
            k.setAktif(false);
            kategoriRepository.save(k);
        });
    }

    @Transactional
    public void kategoriKaliciOlarakSil(int kategoriID) {
        kategoriRepository.deleteById(kategoriID);
    }
}