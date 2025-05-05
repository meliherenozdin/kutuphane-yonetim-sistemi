package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.model.Rezervasyon;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.KitapRepository;
import com.ekip.yakupmelih.library_manage_system.repository.RezervasyonRepository;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RezervasyonService {

    private final RezervasyonRepository rezervasyonRepository;
    private final KitapRepository kitapRepository;
    private final UyeRepository uyeRepository;

    public RezervasyonService(RezervasyonRepository rezervasyonRepository, KitapRepository kitapRepository,
            UyeRepository uyeRepository) {
        this.rezervasyonRepository = rezervasyonRepository;
        this.kitapRepository = kitapRepository;
        this.uyeRepository = uyeRepository;
    }

    public List<Rezervasyon> tumRezervasyonlariGetir() {
        return rezervasyonRepository.findAll();
    }

    public Optional<Rezervasyon> rezervasyonGetir(int id) {
        return rezervasyonRepository.findById(id);
    }

    @Transactional
    public Rezervasyon rezervasyonKaydet(Rezervasyon rezervasyon) {
        return rezervasyonRepository.save(rezervasyon);
    }

    @Transactional
    public void kaydet(int uyeId, int kitapId) {
        Kitap kitap = kitapRepository.findById(kitapId).orElseThrow();
        Uye uye = uyeRepository.findById(uyeId).orElseThrow();

        boolean zatenVar = rezervasyonRepository.findByUye(uye).stream()
                .anyMatch(r -> r.getKitap().equals(kitap) && r.getDurum() == Rezervasyon.RezervasyonDurum.AKTIF);

        if (zatenVar) {
            throw new IllegalStateException("Bu kitap için zaten aktif bir rezervasyonunuz var.");
        }

        Rezervasyon rezervasyon = new Rezervasyon();
        rezervasyon.setKitap(kitap);
        rezervasyon.setUye(uye);
        rezervasyon.setRezervasyonTarih(LocalDate.now());
        rezervasyon.setDurum(Rezervasyon.RezervasyonDurum.AKTIF);
        rezervasyon.setAktif(true);

        rezervasyonRepository.save(rezervasyon);
    }

    public List<Rezervasyon> aktifRezervasyonlariGetir() {
        return rezervasyonRepository.findByAktifTrue();
    }

    public List<Rezervasyon> durumunaGoreRezervasyonlariGetir(Rezervasyon.RezervasyonDurum durum) {
        return rezervasyonRepository.findByDurum(durum);
    }

    public List<Rezervasyon> kitapBazliRezervasyonlariGetir(Kitap kitap) {
        return rezervasyonRepository.findByKitap(kitap);
    }

    public List<Rezervasyon> uyeRezervasyonlariGetir(int uyeId) {
        Uye uye = uyeRepository.findById(uyeId).orElseThrow();
        return rezervasyonRepository.findByUye(uye);
    }

    public Optional<Rezervasyon> rezervasyonBulById(int id) {
        return rezervasyonRepository.findById(id);
    }

    public Rezervasyon rezervasyonGuncelle(int id, Rezervasyon yeniRezervasyon) {
        return rezervasyonRepository.findById(id)
                .map(r -> {
                    r.setKitap(yeniRezervasyon.getKitap());
                    r.setUye(yeniRezervasyon.getUye());
                    r.setRezervasyonTarih(yeniRezervasyon.getRezervasyonTarih());
                    r.setDurum(yeniRezervasyon.getDurum());
                    r.setAciklama(yeniRezervasyon.getAciklama());
                    r.setAktif(yeniRezervasyon.isAktif());
                    return rezervasyonRepository.save(r);
                })
                .orElseThrow();
    }

    public void rezervasyonSil(int id) {
        rezervasyonRepository.findById(id).ifPresent(r -> {
            r.setAktif(false);
            rezervasyonRepository.save(r);
        });
    }
}
