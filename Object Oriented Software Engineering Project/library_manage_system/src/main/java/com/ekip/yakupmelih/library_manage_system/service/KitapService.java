package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Kategori;
import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.repository.KitapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Kitap> aktifKitaplariGetir() {
        return kitapRepository.findByAktifTrue();
    }

    public List<Kitap> durumunaGoreKitaplariGetir(Kitap.KitapDurum durum) {
        return kitapRepository.findByDurum(durum);
    }

    public List<Kitap> stoktaOlanKitaplariGetir() {
        return kitapRepository.findByAdetGreaterThan(0);
    }

    public List<Kitap> kategoriyeGoreAktifKitaplariGetir(Kategori kategori) {
        return kitapRepository.findByKategoriAndAktifTrue(kategori);
    }

    public Optional<Kitap> kitapBulById(int id) {
        return kitapRepository.findById(id);
    }

    public Optional<Kitap> kitapBulByIsbn(String isbn) {
        return kitapRepository.findByIsbn(isbn);
    }

    public Optional<Kitap> kitapBulByAd(String kitapAdi) {
        return kitapRepository.findByKitapAdiContainingIgnoreCase(kitapAdi);
    }

    @Transactional
    public List<Kitap> topluKitapEkle(List<Kitap> kitaplar) {
        return kitapRepository.saveAll(kitaplar);
    }

    @Transactional
    public List<Kitap> topluKitapGuncelle(List<Kitap> kitaplar) {
        return kitapRepository.saveAll(kitaplar);
    }

    public Kitap kitapEkle(Kitap kitap) {
        return kitapRepository.save(kitap);
    }

    public Kitap kitapGuncelle(int id, Kitap yeniKitap) {
        return kitapRepository.findById(id)
                .map(k -> {
                    k.setKitapAdi(yeniKitap.getKitapAdi());
                    k.setIsbn(yeniKitap.getIsbn());
                    k.setBasimYili(yeniKitap.getBasimYili());
                    k.setYayinevi(yeniKitap.getYayinevi());
                    k.setAdet(yeniKitap.getAdet());
                    k.setKategori(yeniKitap.getKategori());
                    k.setYazar(yeniKitap.getYazar());
                    k.setRaf(yeniKitap.getRaf());
                    k.setDurum(yeniKitap.getDurum());
                    k.setAciklama(yeniKitap.getAciklama());
                    k.setAktif(yeniKitap.isAktif());
                    return kitapRepository.save(k);
                })
                .orElseThrow();
    }

    public void kitapSil(int id) {
        kitapRepository.findById(id).ifPresent(k -> {
            k.setAktif(false);
            kitapRepository.save(k);
        });
    }
}
