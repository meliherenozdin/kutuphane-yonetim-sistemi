package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UyeService {

    private final UyeRepository uyeRepository;

    public UyeService(UyeRepository uyeRepository) {
        this.uyeRepository = uyeRepository;
    }

    public List<Uye> tumUyeleriGetir() {
        return uyeRepository.findAll();
    }

    public Optional<Uye> uyeBulById(int id) {
        return uyeRepository.findById(id);
    }

    public Optional<Uye> uyeBulByEmail(String email) {
        return uyeRepository.findByEmail(email);
    }

    public Optional<Uye> uyeBulByTelNo(String telNo) {
        return uyeRepository.findByTelNo(telNo);
    }

    public Uye uyeEkle(Uye uye) {
        return uyeRepository.save(uye);
    }

    public void uyeSil(int id) {
        uyeRepository.deleteById(id);
    }
}
