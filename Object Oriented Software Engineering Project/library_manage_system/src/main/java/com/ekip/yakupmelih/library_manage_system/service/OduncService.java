package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Odunc;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.repository.OduncRepository;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;
import com.ekip.yakupmelih.library_manage_system.repository.KitapRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OduncService {

    private final OduncRepository oduncRepository;
    private final UyeRepository uyeRepository;
    private final KitapRepository kitapRepository;

    public OduncService(OduncRepository oduncRepository, UyeRepository uyeRepository, KitapRepository kitapRepository) {
        this.oduncRepository = oduncRepository;
        this.uyeRepository = uyeRepository;
        this.kitapRepository = kitapRepository;
    }

    public List<Odunc> getOduncByUyeId(int uyeId) {
        Uye uye = uyeRepository.findById(uyeId).orElseThrow();
        return oduncRepository.findByUye(uye);
    }

    public List<Odunc> aktifOdunclariGetir() {
        return oduncRepository.findByAktifTrue();
    }

    public List<Odunc> durumunaGoreOdunclariGetir(Odunc.OduncDurum durum) {
        return oduncRepository.findByDurum(durum);
    }

    public List<Odunc> kitapBazliOdunclariGetir(Kitap kitap) {
        return oduncRepository.findByKitap(kitap);
    }

    public List<Odunc> gecikmisOdunclariGetir(LocalDate tarih) {
        return oduncRepository.findBySonTeslimTarihiBefore(tarih);
    }

    public Optional<Odunc> oduncBulById(int id) {
        return oduncRepository.findById(id);
    }

    @Transactional
    public void oduncAl(int uyeId, int kitapId) {
        Kitap kitap = kitapRepository.findById(kitapId).orElseThrow();
        if (kitap.getAdet() <= 0)
            throw new IllegalStateException("Stokta kitap yok!");

        Uye uye = uyeRepository.findById(uyeId).orElseThrow();

        // Kitap zaten bu üyeye ödünç verilmişse hata fırlat kaldıralabilir bu kısım
        boolean kitapZatenOduncte = oduncRepository.findByKitap(kitap).stream()
                .anyMatch(o -> o.getUye().equals(uye) && o.getDurum() == Odunc.OduncDurum.AKTIF);

        if (kitapZatenOduncte) {
            throw new IllegalStateException("Bu kitap zaten bu üyeye ödünç verilmiş.");
        }

        kitap.setAdet(kitap.getAdet() - 1);
        kitapRepository.save(kitap);

        Odunc odunc = new Odunc();
        odunc.setUye(uye);
        odunc.setKitap(kitap);
        odunc.setOduncAlmaTarih(LocalDate.now());
        odunc.setSonTeslimTarihi(LocalDate.now().plusDays(15));
        odunc.setDurum(Odunc.OduncDurum.AKTIF);
        odunc.setAktif(true);
        oduncRepository.save(odunc);
    }

    @Transactional
    public void kitapIadeEt(int oduncId) {
        Odunc odunc = oduncRepository.findById(oduncId).orElseThrow();

        if (odunc.getDurum() == Odunc.OduncDurum.TESLIM_EDILDI)
            throw new IllegalStateException("Kitap zaten iade edilmiş!");

        // if (odunc.getDurum() != Odunc.OduncDurum.AKTIF)
        // throw new IllegalStateException("Bu kitap zaten iade edilmiş.");
        // Aralarındaki fark teslim edildiyi kontrol ederken yorumdaki kısım aktif
        // dışlar gibisinden daha kapsamlı bir yapı

        Kitap kitap = odunc.getKitap();
        kitap.setAdet(kitap.getAdet() + 1);
        kitapRepository.save(kitap);

        odunc.setGercekTeslimTarih(LocalDate.now());
        odunc.setDurum(Odunc.OduncDurum.TESLIM_EDILDI);
        odunc.setAktif(false);
        oduncRepository.save(odunc);
    }

    public Odunc oduncEkle(Odunc odunc) {
        return oduncRepository.save(odunc);
    }

    public Odunc oduncGuncelle(int id, Odunc yeniOdunc) {
        return oduncRepository.findById(id)
                .map(o -> {
                    o.setUye(yeniOdunc.getUye());
                    o.setKitap(yeniOdunc.getKitap());
                    o.setOduncAlmaTarih(yeniOdunc.getOduncAlmaTarih());
                    o.setGercekTeslimTarih(yeniOdunc.getGercekTeslimTarih());
                    o.setSonTeslimTarihi(yeniOdunc.getSonTeslimTarihi());
                    o.setDurum(yeniOdunc.getDurum());
                    o.setAciklama(yeniOdunc.getAciklama());
                    o.setAktif(yeniOdunc.isAktif());
                    return oduncRepository.save(o);
                })
                .orElseThrow();
    }

    public void oduncSil(int id) {
        oduncRepository.findById(id).ifPresent(o -> {
            o.setAktif(false);
            oduncRepository.save(o);
        });
    }
}
