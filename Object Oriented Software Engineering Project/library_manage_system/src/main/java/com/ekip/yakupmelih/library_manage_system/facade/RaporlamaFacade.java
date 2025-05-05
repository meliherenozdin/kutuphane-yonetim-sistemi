package com.ekip.yakupmelih.library_manage_system.facade;

import com.ekip.yakupmelih.library_manage_system.model.*;
import com.ekip.yakupmelih.library_manage_system.service.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
        
        Uye uye = uyeService.uyeBulById(uyeId)
                .orElseThrow(() -> new RuntimeException("Üye bulunamadı"));
        
        rapor.put("uyeBilgileri", uye);
        rapor.put("aktifOduncler", oduncService.findByUyeId(uyeId));
        rapor.put("aktifCezalar", cezaService.uyeCezalariGetir(uyeId));
        rapor.put("aktifRezervasyonlar", rezervasyonService.uyeRezervasyonlariGetir(uyeId));
        rapor.put("bagislar", bagisService.bagisciBagislariGetir(uyeId));
        
        return rapor;
    }

    public Map<String, Object> kitapDetayRaporu(int kitapId) {
        Map<String, Object> rapor = new HashMap<>();
        
        Kitap kitap = kitapService.kitapGetir(kitapId)
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı"));
        
        rapor.put("kitapBilgileri", kitap);
        rapor.put("aktifOduncler", oduncService.kitapBazliOdunclariGetir(kitap));
        rapor.put("aktifRezervasyonlar", rezervasyonService.kitapBazliRezervasyonlariGetir(kitap));
        
        return rapor;
    }

    public Map<String, Object> tarihAraligiRaporu(LocalDate baslangic, LocalDate bitis) {
        Map<String, Object> rapor = new HashMap<>();
        
        // Ödünç işlemleri
        List<Odunc> oduncler = oduncService.findByUyeId(0); // Tüm ödünçleri al
        oduncler = oduncler.stream()
                .filter(o -> !o.getOduncAlmaTarih().isBefore(baslangic) && !o.getOduncAlmaTarih().isAfter(bitis))
                .toList();
        rapor.put("oduncler", oduncler);
        
        // Ceza işlemleri
        rapor.put("cezalar", cezaService.tarihAraliginaGoreCezalariGetir(baslangic, bitis));
        
        // Rezervasyon işlemleri
        List<Rezervasyon> rezervasyonlar = rezervasyonService.aktifRezervasyonlariGetir();
        rezervasyonlar = rezervasyonlar.stream()
                .filter(r -> !r.getRezervasyonTarih().isBefore(baslangic) && !r.getRezervasyonTarih().isAfter(bitis))
                .toList();
        rapor.put("rezervasyonlar", rezervasyonlar);
        
        // Bağış işlemleri
        List<Bagis> bagislar = bagisService.tumBagislariGetir();
        bagislar = bagislar.stream()
                .filter(b -> !b.getBagisTarih().isBefore(baslangic) && !b.getBagisTarih().isAfter(bitis))
                .toList();
        rapor.put("bagislar", bagislar);
        
        return rapor;
    }
} 