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

    public Optional<Kitap> kitapGetir(int id) {
        return kitapRepository.findById(id);
    }

    public Optional<Kitap> kitapGetirByIsbn(String isbn) {
        return kitapRepository.findByIsbn(isbn);
    }

    public List<Kitap> kitapAra(String kitapAdi) {
        return kitapRepository.findByKitapAdiContainingIgnoreCase(kitapAdi);
    }

    @Transactional
    public List<Kitap> topluKitapKaydet(List<Kitap> kitaplar) {
        return kitapRepository.saveAll(kitaplar);
    }

    @Transactional
    public Kitap kitapKaydet(Kitap kitap) {
        return kitapRepository.save(kitap);
    }

    @Transactional
    public Kitap kitapGuncelle(int id, Kitap kitap) {
        return kitapRepository.findById(id)
                .map(k -> {
                    k.setKitapAdi(kitap.getKitapAdi());
                    k.setIsbn(kitap.getIsbn());
                    k.setBasimYili(kitap.getBasimYili());
                    k.setYayinevi(kitap.getYayinevi());
                    k.setAdet(kitap.getAdet());
                    k.setKategori(kitap.getKategori());
                    k.setYazar(kitap.getYazar());
                    k.setRaf(kitap.getRaf());
                    k.setDurum(kitap.getDurum());
                    k.setAciklama(kitap.getAciklama());
                    k.setAktif(kitap.isAktif());
                    return kitapRepository.save(k);
                })
                .orElseThrow();
    }

    @Transactional
    public void kitapSil(int id) {
        kitapRepository.findById(id).ifPresent(k -> {
            k.setAktif(false);
            kitapRepository.save(k);
        });
    }

    @Transactional
    public Kitap kitapEkle(Kitap kitap) {
        return kitapRepository.save(kitap);
    }

    public Optional<Kitap> kitapBulById(int id) {
        return kitapRepository.findById(id);
    }

    public Optional<Kitap> kitapBulByIsbn(String isbn) {
        return kitapRepository.findByIsbn(isbn);
    }

    @Transactional
    public Kitap guncelle(Kitap kitap) {
        return kitapRepository.save(kitap);
    }
}
