package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Raf;
import com.ekip.yakupmelih.library_manage_system.repository.RafRepository;
import org.springframework.stereotype.Service;

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

    public Optional<Raf> rafGetir(String id) {
        return rafRepository.findById(id);
    }

    public Raf rafKaydet(Raf raf) {
        return rafRepository.save(raf);
    }

    public void rafSil(String id) {
        rafRepository.deleteById(id);
    }

    public Raf rafGuncelle(String id, Raf raf) {
        if (rafRepository.existsById(id)) {
            raf.setRafNo(id);
            return rafRepository.save(raf);
        }
        throw new RuntimeException("Raf bulunamadı: " + id);
    }

    public List<Raf> rafAra(String kod) {
        return rafRepository.findByRafNoContaining(kod);
    }
} 