package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Bagis;
import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.BagisRepository;
import com.ekip.yakupmelih.library_manage_system.repository.KitapRepository;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;

import java.time.LocalDate;

public class BagisService {

    private final BagisRepository bagisRepository;
    private final UyeRepository uyeRepository;
    private final KitapRepository kitapRepository;

    public BagisService(BagisRepository bagisRepository, UyeRepository uyeRepository, KitapRepository kitapRepository) {
        this.bagisRepository = bagisRepository;
        this.uyeRepository = uyeRepository;
        this.kitapRepository = kitapRepository;
    }

    public void bagisKaydet(int uyeId, int kitapId) {
        Uye uye = uyeRepository.findById(uyeId).orElseThrow();
        Kitap kitap = kitapRepository.findById(kitapId).orElseThrow();

        Bagis bagis = new Bagis();
        bagis.setUye(uye);
        bagis.setKitap(kitap);
        bagis.setBagisTarih(LocalDate.now());

        bagisRepository.save(bagis);
    }
}
