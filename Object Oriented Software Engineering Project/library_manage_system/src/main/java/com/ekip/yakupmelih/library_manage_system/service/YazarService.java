package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Yazar;
import com.ekip.yakupmelih.library_manage_system.repository.YazarRepository;
import org.springframework.stereotype.Service;

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

    public Optional<Yazar> yazarGetir(int id) {
        return yazarRepository.findById(id);
    }

    public Yazar yazarKaydet(Yazar yazar) {
        return yazarRepository.save(yazar);
    }

    public void yazarSil(int id) {
        yazarRepository.deleteById(id);
    }

    public Yazar yazarGuncelle(int id, Yazar yazar) {
        if (yazarRepository.existsById(id)) {
            yazar.setYazarID(id);
            return yazarRepository.save(yazar);
        }
        throw new RuntimeException("Yazar bulunamadı: " + id);
    }

    public List<Yazar> yazarAra(String ad) {
        return yazarRepository.findByYazarAdiContaining(ad);
    }
} 