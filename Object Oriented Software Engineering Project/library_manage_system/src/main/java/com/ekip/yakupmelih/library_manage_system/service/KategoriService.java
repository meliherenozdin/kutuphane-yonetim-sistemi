package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Kategori;
import com.ekip.yakupmelih.library_manage_system.repository.KategoriRepository;
import org.springframework.stereotype.Service;

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

    public Optional<Kategori> kategoriGetir(int id) {
        return kategoriRepository.findById(id);
    }

    public Kategori kategoriKaydet(Kategori kategori) {
        return kategoriRepository.save(kategori);
    }

    public void kategoriSil(int id) {
        kategoriRepository.deleteById(id);
    }

    public Kategori kategoriGuncelle(int id, Kategori kategori) {
        if (kategoriRepository.existsById(id)) {
            kategori.setKategoriID(id);
            return kategoriRepository.save(kategori);
        }
        throw new RuntimeException("Kategori bulunamadı: " + id);
    }

    public List<Kategori> kategoriAra(String ad) {
        return kategoriRepository.findByTuruContaining(ad);
    }
} 