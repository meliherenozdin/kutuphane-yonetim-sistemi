package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.repository.KitapRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KitapService {

    private final KitapRepository kitapRepository;

    public KitapService(KitapRepository kitapRepository) {
        this.kitapRepository = kitapRepository;
    }

    public List<Kitap> tumKitaplariGetir() {
        return kitapRepository.findAll();
    }

    public Optional<Kitap> kitapBulById(int id) {
        return kitapRepository.findById(id);
    }

    public Optional<Kitap> kitapBulByIsbn(String isbn) {
        return kitapRepository.findByIsbn(isbn);
    }

    public Kitap kitapEkle(Kitap kitap) {
        return kitapRepository.save(kitap);
    }

    public void kitapSil(int id) {
        kitapRepository.deleteById(id);
    }
}
