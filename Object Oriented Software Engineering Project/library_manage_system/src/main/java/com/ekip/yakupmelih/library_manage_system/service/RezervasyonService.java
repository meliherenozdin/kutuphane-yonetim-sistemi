package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.model.Rezervasyon;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.KitapRepository;
import com.ekip.yakupmelih.library_manage_system.repository.RezervasyonRepository;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;

import java.time.LocalDate;

public class RezervasyonService {

    private final RezervasyonRepository rezervasyonRepository;
    private final KitapRepository kitapRepository;
    private final UyeRepository uyeRepository;

    public RezervasyonService(RezervasyonRepository rezervasyonRepository, KitapRepository kitapRepository, UyeRepository uyeRepository) {
        this.rezervasyonRepository = rezervasyonRepository;
        this.kitapRepository = kitapRepository;
        this.uyeRepository = uyeRepository;
    }

    public void kaydet(int uyeId, int kitapId) {
        Kitap kitap = kitapRepository.findById(kitapId).orElseThrow();
        Uye uye = uyeRepository.findById(uyeId).orElseThrow();

        Rezervasyon rezervasyon = new Rezervasyon();
        rezervasyon.setKitap(kitap);
        rezervasyon.setUye(uye);
        rezervasyon.setRezervasyonTarih(LocalDate.now());
        rezervasyon.setDurum("Beklemede");

        rezervasyonRepository.save(rezervasyon);
    }
}
