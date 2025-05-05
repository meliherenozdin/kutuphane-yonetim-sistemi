package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Bagis;
import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.BagisRepository;
import com.ekip.yakupmelih.library_manage_system.repository.KitapRepository;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BagisService {

    private final BagisRepository bagisRepository;
    private final UyeRepository uyeRepository;
    private final KitapRepository kitapRepository;

    public BagisService(BagisRepository bagisRepository, UyeRepository uyeRepository, KitapRepository kitapRepository) {
        this.bagisRepository = bagisRepository;
        this.uyeRepository = uyeRepository;
        this.kitapRepository = kitapRepository;
    }

    @Transactional
    public void bagisKaydet(int uyeId, int kitapId, int adet) {
        Uye uye = uyeRepository.findById(uyeId).orElseThrow();
        Kitap kitap = kitapRepository.findById(kitapId).orElseThrow();

        Bagis bagis = new Bagis();
        bagis.setUye(uye);
        bagis.setKitap(kitap);
        bagis.setBagisTarih(LocalDate.now());
        bagis.setAktif(true);
        bagis.setAdet(adet);
        bagisRepository.save(bagis);

        kitap.setAdet(kitap.getAdet() + adet);
        kitapRepository.save(kitap);
    }

    public List<Bagis> aktifBagislariGetir() {
        return bagisRepository.findByAktifTrue();
    }

    public List<Bagis> aciklamaIleBagisAra(String aciklama) {
        return bagisRepository.findByAciklamaContainingIgnoreCase(aciklama);
    }

    public Optional<Bagis> bagisBulById(int id) {
        return bagisRepository.findById(id);
    }

    @Transactional
    public Bagis bagisGuncelle(int id, Bagis bagis) {
        return bagisRepository.findById(id)
                .map(b -> {
                    b.setUye(bagis.getUye());
                    b.setKitap(bagis.getKitap());
                    b.setBagisTarih(bagis.getBagisTarih());
                    b.setAdet(bagis.getAdet());
                    b.setAciklama(bagis.getAciklama());
                    b.setAktif(bagis.isAktif());
                    return bagisRepository.save(b);
                })
                .orElseThrow();
    }

    @Transactional
    public void bagisSil(int id) {
        bagisRepository.findById(id).ifPresent(b -> {
            b.setAktif(false);
            bagisRepository.save(b);
        });
    }

    public List<Bagis> tumBagislariGetir() {
        return bagisRepository.findAll();
    }

    public Optional<Bagis> bagisGetir(int id) {
        return bagisRepository.findById(id);
    }

    @Transactional
    public Bagis bagisKaydet(Bagis bagis) {
        return bagisRepository.save(bagis);
    }

    public List<Bagis> bagisciBagislariGetir(int bagisciId) {
        return bagisRepository.findByUye_UyeID(bagisciId);
    }
}
