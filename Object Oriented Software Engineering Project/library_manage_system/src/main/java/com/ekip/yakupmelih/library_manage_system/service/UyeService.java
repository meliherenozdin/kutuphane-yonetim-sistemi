package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Uye> aktifUyeleriGetir() {
        return uyeRepository.findByAktifTrue();
    }

    public List<Uye> durumunaGoreUyeleriGetir(Uye.UyeDurum durum) {
        return uyeRepository.findByDurum(durum);
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

    @Transactional
    public List<Uye> topluUyeEkle(List<Uye> uyeler) {
        return uyeRepository.saveAll(uyeler);
    }

    @Transactional
    public List<Uye> topluUyeGuncelle(List<Uye> uyeler) {
        return uyeRepository.saveAll(uyeler);
    }

    public Uye uyeEkle(Uye uye) {
        return uyeRepository.save(uye);
    }

    public Uye uyeGuncelle(int id, Uye yeniUye) {
        return uyeRepository.findById(id)
                .map(u -> {
                    u.setUyeAdi(yeniUye.getUyeAdi());
                    u.setUyeSoyad(yeniUye.getUyeSoyad());
                    u.setEmail(yeniUye.getEmail());
                    u.setTelNo(yeniUye.getTelNo());
                    u.setAdres(yeniUye.getAdres());
                    u.setDurum(yeniUye.getDurum());
                    u.setAciklama(yeniUye.getAciklama());
                    u.setAktif(yeniUye.isAktif());
                    return uyeRepository.save(u);
                })
                .orElseThrow();
    }

    public void uyeSil(int id) {
        uyeRepository.findById(id).ifPresent(u -> {
            u.setAktif(false);
            uyeRepository.save(u);
        });
    }
}
