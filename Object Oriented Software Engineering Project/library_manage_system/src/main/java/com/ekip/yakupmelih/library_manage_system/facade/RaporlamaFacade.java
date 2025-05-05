package com.ekip.yakupmelih.library_manage_system.facade;

import com.ekip.yakupmelih.library_manage_system.model.*;
import com.ekip.yakupmelih.library_manage_system.service.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@Component
public class RaporlamaFacade {
    private final KitapService kitapService;
    private final OduncService oduncService;
    private final UyeService uyeService;
    private final CezaService cezaService;
    private final RezervasyonService rezervasyonService;
    private final BagisService bagisService;

    public RaporlamaFacade(
            KitapService kitapService,
            OduncService oduncService,
            UyeService uyeService,
            CezaService cezaService,
            RezervasyonService rezervasyonService,
            BagisService bagisService) {
        this.kitapService = kitapService;
        this.oduncService = oduncService;
        this.uyeService = uyeService;
        this.cezaService = cezaService;
        this.rezervasyonService = rezervasyonService;
        this.bagisService = bagisService;
    }

    public Map<String, Object> genelDurumRaporu() {
        Map<String, Object> rapor = new HashMap<>();

        // Kitap istatistikleri
        rapor.put("toplamKitapSayisi", kitapService.tumKitaplariGetir().size());
        rapor.put("stoktaOlanKitapSayisi", kitapService.stoktaOlanKitaplariGetir().size());

        // Üye istatistikleri
        rapor.put("toplamUyeSayisi", uyeService.tumUyeleriGetir().size());
        rapor.put("aktifUyeSayisi", uyeService.aktifUyeleriGetir().size());

        // Ödünç istatistikleri
        rapor.put("aktifOduncSayisi", oduncService.aktifOdunclariGetir().size());
        rapor.put("gecikmisOduncSayisi", oduncService.gecikmisOdunclariGetir(LocalDate.now()).size());

        // Ceza istatistikleri
        rapor.put("aktifCezaSayisi", cezaService.aktifCezalariGetir().size());

        // Rezervasyon istatistikleri
        rapor.put("aktifRezervasyonSayisi", rezervasyonService.aktifRezervasyonlariGetir().size());

        return rapor;
    }

    public Map<String, Object> uyeDetayRaporu(int uyeId) {
        Map<String, Object> rapor = new HashMap<>();

        var uyeOptional = uyeService.uyeBulById(uyeId);
        if (uyeOptional.isEmpty()) {
            rapor.put("uyeBilgileri", "Üye bulunamadı (ID: " + uyeId + ")");
            rapor.put("aktifOduncler", "Üye bulunamadı");
            rapor.put("aktifCezalar", "Üye bulunamadı");
            rapor.put("aktifRezervasyonlar", "Üye bulunamadı");
            rapor.put("bagislar", "Üye bulunamadı");
            return rapor;
        }

        Uye uye = uyeOptional.get();

        // Üye bilgileri
        rapor.put("uyeBilgileri", String.format("""

                === ÜYE BİLGİLERİ ===
                Üye ID: %d
                Ad Soyad: %s %s
                Email: %s
                Telefon: %s
                Üyelik Tarihi: %s
                Durum: %s
                """,
                uye.getUyeID(),
                uye.getUyeAdi(),
                uye.getUyeSoyad(),
                uye.getEmail(),
                uye.getTelNo(),
                uye.getCreatedAt(),
                uye.isAktif() ? "Aktif" : "Pasif"));

        // Aktif ödünçler
        List<Odunc> aktifOduncler = oduncService.findByUyeId(uyeId);
        rapor.put("aktifOduncler", String.format("""

                === AKTİF ÖDÜNÇ ALINAN KİTAPLAR (%d adet) ===
                %s""",
                aktifOduncler.size(),
                aktifOduncler.isEmpty() ? "Aktif ödünç alınan kitap bulunmamaktadır.\n"
                        : aktifOduncler.stream()
                                .map(o -> String.format("Kitap: %s | Alış Tarihi: %s | İade Tarihi: %s",
                                        o.getKitap() != null ? o.getKitap().getKitapAdi() : "Bilinmiyor",
                                        o.getOduncAlmaTarih(),
                                        o.getSonTeslimTarihi()))
                                .collect(Collectors.joining("\n"))));

        // Aktif cezalar
        List<Ceza> aktifCezalar = cezaService.uyeCezalariGetir(uyeId);
        rapor.put("aktifCezalar", String.format("""

                === AKTİF CEZALAR (%d adet) ===
                %s""",
                aktifCezalar.size(),
                aktifCezalar.isEmpty() ? "Aktif ceza bulunmamaktadır.\n"
                        : aktifCezalar.stream()
                                .map(c -> String.format("Ceza Nedeni: %s | Başlangıç: %s | Bitiş: %s | Tutar: %.2f TL",
                                        c.getAciklama(),
                                        c.getCezaTarih(),
                                        c.getUpdatedAt(),
                                        c.getCezaMiktar()))
                                .collect(Collectors.joining("\n"))));

        // Aktif rezervasyonlar
        List<Rezervasyon> aktifRezervasyonlar = rezervasyonService.uyeRezervasyonlariGetir(uyeId);
        rapor.put("aktifRezervasyonlar", String.format("""

                === AKTİF REZERVASYONLAR (%d adet) ===
                %s""",
                aktifRezervasyonlar.size(),
                aktifRezervasyonlar.isEmpty() ? "Aktif rezervasyon bulunmamaktadır.\n"
                        : aktifRezervasyonlar.stream()
                                .map(r -> String.format("Kitap: %s | Rezervasyon Tarihi: %s",
                                        r.getKitap() != null ? r.getKitap().getKitapAdi() : "Bilinmiyor",
                                        r.getRezervasyonTarih()))
                                .collect(Collectors.joining("\n"))));

        // Bağışlar
        List<Bagis> bagislar = bagisService.bagisciBagislariGetir(uyeId);
        rapor.put("bagislar", String.format("""

                === YAPILAN BAĞIŞLAR (%d adet) ===
                %s""",
                bagislar.size(),
                bagislar.isEmpty() ? "Yapılan bağış bulunmamaktadır.\n"
                        : bagislar.stream()
                                .map(b -> String.format("Kitap: %s | Adet: %d | Tarih: %s",
                                        b.getKitap() != null ? b.getKitap().getKitapAdi() : "Bilinmiyor",
                                        b.getAdet(),
                                        b.getBagisTarih()))
                                .collect(Collectors.joining("\n"))));

        return rapor;
    }

    public Map<String, Object> kitapDetayRaporu(int kitapId) {
        Map<String, Object> rapor = new HashMap<>();

        var kitapOptional = kitapService.kitapGetir(kitapId);
        if (kitapOptional.isEmpty()) {
            rapor.put("kitapBilgileri", "Kitap bulunamadı (ID: " + kitapId + ")");
            rapor.put("aktifOduncler", "Kitap bulunamadı");
            rapor.put("aktifRezervasyonlar", "Kitap bulunamadı");
            return rapor;
        }

        Kitap kitap = kitapOptional.get();

        // Format kitap bilgileri
        rapor.put("kitapBilgileri", String.format("""

                === KİTAP BİLGİLERİ ===
                Kitap ID: %d
                Kitap Adı: %s
                ISBN: %s
                Basım Yılı: %s
                Yayınevi: %s
                Stok Adedi: %d
                Durum: %s
                """,
                kitap.getKitapID(),
                kitap.getKitapAdi(),
                kitap.getIsbn(),
                kitap.getBasimYili(),
                kitap.getYayinevi() != null ? kitap.getYayinevi() : "Belirtilmemiş",
                kitap.getAdet(),
                kitap.getDurum()));

        // Format aktif ödünçler
        List<Odunc> aktifOduncler = oduncService.kitapBazliOdunclariGetir(kitap);
        rapor.put("aktifOduncler", String.format("""

                === ÖDÜNÇ ALMA BİLGİLERİ (%d adet) ===
                %s""",
                aktifOduncler.size(),
                aktifOduncler.isEmpty() ? "Bu kitap şu anda hiç kimse tarafından ödünç alınmamış.\n"
                        : aktifOduncler.stream()
                                .map(o -> String.format("Üye: %s | Alış Tarihi: %s | İade Tarihi: %s",
                                        o.getUye() != null ? o.getUye().getUyeAdi() + " " + o.getUye().getUyeSoyad()
                                                : "Bilinmiyor",
                                        o.getOduncAlmaTarih(),
                                        o.getSonTeslimTarihi()))
                                .collect(Collectors.joining("\n"))));

        // Format aktif rezervasyonlar
        List<Rezervasyon> aktifRezervasyonlar = rezervasyonService.kitapBazliRezervasyonlariGetir(kitap);
        rapor.put("aktifRezervasyonlar", String.format("""

                === REZERVASYON BİLGİLERİ (%d adet) ===
                %s""",
                aktifRezervasyonlar.size(),
                aktifRezervasyonlar.isEmpty() ? "Bu kitap için aktif rezervasyon bulunmamaktadır.\n"
                        : aktifRezervasyonlar.stream()
                                .map(r -> String.format("Üye: %s | Rezervasyon Tarihi: %s",
                                        r.getUye() != null ? r.getUye().getUyeAdi() + " " + r.getUye().getUyeSoyad()
                                                : "Bilinmiyor",
                                        r.getRezervasyonTarih()))
                                .collect(Collectors.joining("\n"))));

        return rapor;
    }

    public Map<String, Object> tarihAraligiRaporu(LocalDate baslangic, LocalDate bitis) {
        Map<String, Object> rapor = new HashMap<>();

        try {
            // Ödünç işlemleri
            List<Odunc> oduncler;
            try {
                oduncler = oduncService.tumOduncleriGetir();
            } catch (Exception e) {
                oduncler = List.of(); // Boş liste oluştur
            }

            List<Odunc> filtrelenmisOduncler = oduncler.stream()
                    .filter(o -> !o.getOduncAlmaTarih().isBefore(baslangic) && !o.getOduncAlmaTarih().isAfter(bitis))
                    .toList();

            rapor.put("oduncler", String.format("""

                    === ÖDÜNÇ İŞLEMLERİ (%d adet) ===
                    %s""",
                    filtrelenmisOduncler.size(),
                    filtrelenmisOduncler.isEmpty() ? "Bu tarih aralığında ödünç işlemi bulunmamaktadır.\n"
                            : filtrelenmisOduncler.stream()
                                    .map(o -> String.format("Kitap: %s | Üye: %s | Tarih: %s",
                                            o.getKitap() != null ? o.getKitap().getKitapAdi() : "Bilinmiyor",
                                            o.getUye() != null ? o.getUye().getUyeAdi() + " " + o.getUye().getUyeSoyad()
                                                    : "Bilinmiyor",
                                            o.getOduncAlmaTarih()))
                                    .collect(Collectors.joining("\n"))));

            // Ceza işlemleri
            List<Ceza> cezalar = cezaService.tarihAraliginaGoreCezalariGetir(baslangic, bitis);
            rapor.put("cezalar", String.format("""

                    === CEZA İŞLEMLERİ (%d adet) ===
                    %s""",
                    cezalar.size(),
                    cezalar.isEmpty() ? "Bu tarih aralığında ceza işlemi bulunmamaktadır.\n"
                            : cezalar.stream()
                                    .map(c -> String.format("Üye: %s | Miktar: %.2f TL | Tarih: %s",
                                            c.getUye() != null ? c.getUye().getUyeAdi() + " " + c.getUye().getUyeSoyad()
                                                    : "Bilinmiyor",
                                            c.getCezaMiktar(),
                                            c.getCezaTarih()))
                                    .collect(Collectors.joining("\n"))));

            // Rezervasyon işlemleri
            List<Rezervasyon> rezervasyonlar = rezervasyonService.aktifRezervasyonlariGetir();
            List<Rezervasyon> filtrelenmisRezervasyonlar = rezervasyonlar.stream()
                    .filter(r -> !r.getRezervasyonTarih().isBefore(baslangic)
                            && !r.getRezervasyonTarih().isAfter(bitis))
                    .toList();

            rapor.put("rezervasyonlar", String.format("""

                    === REZERVASYON İŞLEMLERİ (%d adet) ===
                    %s""",
                    filtrelenmisRezervasyonlar.size(),
                    filtrelenmisRezervasyonlar.isEmpty() ? "Bu tarih aralığında rezervasyon işlemi bulunmamaktadır.\n"
                            : filtrelenmisRezervasyonlar.stream()
                                    .map(r -> String.format("Kitap: %s | Üye: %s | Tarih: %s",
                                            r.getKitap() != null ? r.getKitap().getKitapAdi() : "Bilinmiyor",
                                            r.getUye() != null ? r.getUye().getUyeAdi() + " " + r.getUye().getUyeSoyad()
                                                    : "Bilinmiyor",
                                            r.getRezervasyonTarih()))
                                    .collect(Collectors.joining("\n"))));

            // Bağış işlemleri
            List<Bagis> bagislar = bagisService.tumBagislariGetir();
            List<Bagis> filtrelenmisBagislar = bagislar.stream()
                    .filter(b -> !b.getBagisTarih().isBefore(baslangic) && !b.getBagisTarih().isAfter(bitis))
                    .toList();

            rapor.put("bagislar", String.format("""

                    === BAĞIŞ İŞLEMLERİ (%d adet) ===
                    %s""",
                    filtrelenmisBagislar.size(),
                    filtrelenmisBagislar.isEmpty() ? "Bu tarih aralığında bağış işlemi bulunmamaktadır.\n"
                            : filtrelenmisBagislar.stream()
                                    .map(b -> String.format("Kitap: %s | Üye: %s | Adet: %d | Tarih: %s",
                                            b.getKitap() != null ? b.getKitap().getKitapAdi() : "Bilinmiyor",
                                            b.getUye() != null ? b.getUye().getUyeAdi() + " " + b.getUye().getUyeSoyad()
                                                    : "Bilinmiyor",
                                            b.getAdet(),
                                            b.getBagisTarih()))
                                    .collect(Collectors.joining("\n"))));

        } catch (Exception e) {
            rapor.put("hata", "Rapor oluşturulurken bir hata oluştu: " + e.getMessage());
        }

        return rapor;
    }
}