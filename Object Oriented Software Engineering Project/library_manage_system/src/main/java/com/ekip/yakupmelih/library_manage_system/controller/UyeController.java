package com.ekip.yakupmelih.library_manage_system.controller;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ekip.yakupmelih.library_manage_system.repository.KitapRepository;
import com.ekip.yakupmelih.library_manage_system.service.BagisService;
import com.ekip.yakupmelih.library_manage_system.service.CezaService;
import com.ekip.yakupmelih.library_manage_system.service.KitapService;
import com.ekip.yakupmelih.library_manage_system.service.OduncService;
import com.ekip.yakupmelih.library_manage_system.service.RezervasyonService;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.CezaRepository;

@Component
public class UyeController implements KullaniciController.Kullanici {

    private Uye uye;

    @Autowired
    private final KitapService kitapService;

    @Autowired
    private final CezaService cezaService;

    @Autowired
    private final BagisService bagisService;

    @Autowired
    private final OduncService oduncService;

    @Autowired
    private final RezervasyonService rezervasyonService;

    public UyeController(
            KitapService kitapService,
            CezaService cezaService,
            BagisService bagisService,
            OduncService oduncService,
            RezervasyonService rezervasyonService) {
        this.kitapService = kitapService;
        this.cezaService = cezaService;
        this.bagisService = bagisService;
        this.oduncService = oduncService;
        this.rezervasyonService = rezervasyonService;
    }

    public void setUye(Uye uye) {
        this.uye = uye;
    }

    @Override
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Üye Menü ===");
            System.out.println("1. Kitapları Listele");
            System.out.println("2. Kitap Ara");
            System.out.println("3. Ceza Bilgilerini Görüntüle");
            System.out.println("4. Kitap Bağışla");
            System.out.println("5. Ödünç Alınan Kitaplar");
            System.out.println("6. Kitap Rezerve Et");
            System.out.println("7. Profil Bilgilerimi Görüntüle");
            System.out.println("8. Çıkış");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> kitaplariListele();
                case "2" -> kitapAra();
                case "3" -> cezalar();
                case "4" -> bagisYap();
                case "5" -> odunclariGoruntule();
                case "6" -> rezervasyonYap();
                case "7" -> profilBilgileriniGoruntule();
                case "8" -> {
                    return;
                }
                default -> System.out.println("Geçersiz seçim.");
            }
        }
    }

    private void profilBilgileriniGoruntule() {
        System.out.println("\n=== Profil Bilgileri ===");
        System.out.println("Ad: " + uye.getUyeAdi());
        System.out.println("Soyad: " + uye.getUyeSoyad());
        System.out.println("E-posta: " + uye.getEmail());
        System.out.println("Telefon: " + uye.getTelNo());
        if (uye.getAdres() != null) {
            System.out.println("Adres: " + uye.getAdres());
        }
    }

    private void kitaplariListele() {
        System.out.println("\n=== Tüm Kitaplar ===");
        kitapService.tumKitaplariGetir()
                .forEach(k -> System.out.printf("ID: %d - Ad: %s - Yazar: %s - ISBN: %s%n",
                        k.getKitapID(),
                        k.getKitapAdi(),
                        k.getYazar().getYazarAdi(),
                        k.getIsbn()));
    }

    private void kitapAra() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Kitap Arama ===");
        System.out.println("1. ISBN ile Ara");
        System.out.println("2. İsim ile Ara");
        System.out.print("Seçiminiz: ");
        String secim = scanner.nextLine();

        switch (secim) {
            case "1" -> {
                System.out.print("ISBN: ");
                String isbn = scanner.nextLine();
                kitapService.kitapBulByIsbn(isbn).ifPresentOrElse(
                        kitap -> System.out.printf("Bulundu: %s - Yazar: %s%n",
                                kitap.getKitapAdi(),
                                kitap.getYazar().getYazarAdi()),
                        () -> System.out.println("Kitap bulunamadı."));
            }
            case "2" -> {
                System.out.print("Kitap Adı: ");
                String kitapAdi = scanner.nextLine();
                kitapService.kitapBulByAd(kitapAdi).ifPresentOrElse(
                        kitap -> System.out.printf("Bulundu: %s - Yazar: %s - ISBN: %s%n",
                                kitap.getKitapAdi(),
                                kitap.getYazar().getYazarAdi(),
                                kitap.getIsbn()),
                        () -> System.out.println("Kitap bulunamadı."));
            }
            default -> System.out.println("Geçersiz seçim.");
        }
    }

    private void cezalar() {

        cezaService.getCezaByUyeId(uye.getUyeID())
                .forEach(ceza -> System.out.printf("Ceza: %.2f ₺ - Tarih: %s%n",
                        ceza.getCezaMiktar(), ceza.getCezaTarih()));
    }

    private void bagisYap() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Bağışlanan kitabın ID'sini girin: ");
        int kitapId = Integer.parseInt(scanner.nextLine());

        bagisService.bagisKaydet(uye.getUyeID(), kitapId);
        System.out.println("Bağış işlemi tamamlandı.");
    }

    private void odunclariGoruntule() {
        oduncService.getOduncByUyeId(uye.getUyeID())
                .forEach(odunc -> System.out.printf("Kitap ID: %d - Alınma: %s - Teslim: %s%n",
                        odunc.getKitap().getKitapID(),
                        odunc.getOduncAlmaTarih(),
                        odunc.getGercekTeslimTarih()));
    }

    private void rezervasyonYap() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Rezerve edilecek kitabın ID'sini girin: ");
        int kitapId = Integer.parseInt(scanner.nextLine());

        rezervasyonService.kaydet(uye.getUyeID(), kitapId);
        System.out.println("Rezervasyon tamamlandı.");
    }

}
