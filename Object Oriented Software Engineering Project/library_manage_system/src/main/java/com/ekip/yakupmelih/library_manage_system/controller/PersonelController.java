package com.ekip.yakupmelih.library_manage_system.controller;

import java.util.Scanner;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.ekip.yakupmelih.library_manage_system.service.KitapService;
import com.ekip.yakupmelih.library_manage_system.service.CezaService;
import com.ekip.yakupmelih.library_manage_system.service.BagisService;
import com.ekip.yakupmelih.library_manage_system.service.OduncService;
import com.ekip.yakupmelih.library_manage_system.service.RezervasyonService;
import com.ekip.yakupmelih.library_manage_system.model.Personel;
import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.KategoriRepository;
import com.ekip.yakupmelih.library_manage_system.repository.RafRepository;
import com.ekip.yakupmelih.library_manage_system.repository.YazarRepository;
import com.ekip.yakupmelih.library_manage_system.controller.RaporlamaController;
import com.ekip.yakupmelih.library_manage_system.iterator.UyeIterator;
import com.ekip.yakupmelih.library_manage_system.iterator.AktifUyeIterator;

@Component
public class PersonelController implements KullaniciController.Kullanici {

    private Personel personel;
    private final Scanner scanner = new Scanner(System.in);

    private final KitapService kitapService;
    private final CezaService cezaService;
    private final BagisService bagisService;
    private final OduncService oduncService;
    private final RezervasyonService rezervasyonService;
    private final KategoriRepository kategoriRepository;
    private final YazarRepository yazarRepository;
    private final RafRepository rafRepository;
    private final RaporlamaController raporlamaController;
    private final UyeIterator aktifUyeIterator;

    public PersonelController(
            KitapService kitapService,
            CezaService cezaService,
            BagisService bagisService,
            OduncService oduncService,
            RezervasyonService rezervasyonService,
            KategoriRepository kategoriRepository,
            YazarRepository yazarRepository,
            RafRepository rafRepository,
            RaporlamaController raporlamaController,
            AktifUyeIterator aktifUyeIterator) {
        this.kitapService = kitapService;
        this.cezaService = cezaService;
        this.bagisService = bagisService;
        this.oduncService = oduncService;
        this.rezervasyonService = rezervasyonService;
        this.kategoriRepository = kategoriRepository;
        this.yazarRepository = yazarRepository;
        this.rafRepository = rafRepository;
        this.raporlamaController = raporlamaController;
        this.aktifUyeIterator = aktifUyeIterator;
    }

    public void setPersonel(Personel personel) {
        this.personel = personel;
    }

    @Override
    public void menu() {
        while (true) {
            System.out.println("\n=== Personel Menü ===");
            System.out.println("1. Kitap Ekle");
            System.out.println("2. Kitap Sil");
            System.out.println("3. Kitap Güncelle");
            System.out.println("4. Kitap Ara");
            System.out.println("5. Raporlama Sistemi");
            System.out.println("6. Aktif Üyeleri Listele");
            System.out.println("7. Çıkış");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> kitapEkle();
                case "2" -> kitapSil();
                case "3" -> kitapGuncelle();
                case "4" -> kitapAra();
                case "5" -> raporlamaController.menu("personel");
                case "6" -> aktifUyeleriListele();
                case "7" -> {
                    return;
                }
                default -> System.out.println("Geçersiz seçim.");
            }
        }
    }

    private void kitapEkle() {
        System.out.println(">> Yeni kitap bilgilerini girin:");

        Kitap kitap = new Kitap();
        System.out.print("Kitap Adı: ");
        kitap.setKitapAdi(scanner.nextLine());

        System.out.print("ISBN: ");
        kitap.setIsbn(scanner.nextLine());

        System.out.print("Basım Yılı (yyyy-MM-dd): ");
        String basimStr = scanner.nextLine();

        try {
            kitap.setBasimYili(LocalDate.parse(basimStr));
        } catch (Exception e) {
            System.out.println("Hatalı tarih formatı! Lütfen yyyy-MM-dd şeklinde girin.");
            return;
        }

        System.out.print("Kategori ID: ");
        int kategoriId = Integer.parseInt(scanner.nextLine());
        kitap.setKategori(kategoriRepository.findById(kategoriId).orElse(null));

        System.out.print("Yazar ID: ");
        int yazarId = Integer.parseInt(scanner.nextLine());
        kitap.setYazar(yazarRepository.findById(yazarId).orElse(null));

        System.out.print("Raf No: ");
        String rafNo = scanner.nextLine();
        kitap.setRaf(rafRepository.findById(rafNo).orElse(null));

        System.out.print("Yayınevi: ");
        kitap.setYayinevi(scanner.nextLine());

        System.out.print("Adet: ");
        kitap.setAdet(Integer.parseInt(scanner.nextLine()));

        kitapService.kitapEkle(kitap);
        System.out.println(" Kitap başarıyla eklendi.");
    }

    private void kitapSil() {
        System.out.print("Silinecek kitabın ID'sini girin: ");
        int id = Integer.parseInt(scanner.nextLine());

        kitapService.kitapSil(id);
        System.out.println(" Kitap silindi (veya varsa).");
    }

    private void kitapGuncelle() {
        System.out.print("Güncellenecek kitabın ID'sini girin: ");
        int id = Integer.parseInt(scanner.nextLine());

        Optional<Kitap> kitapOpt = kitapService.kitapBulById(id);
        if (kitapOpt.isPresent()) {
            Kitap kitap = kitapOpt.get();

            System.out.print("Yeni Kitap Adı (" + kitap.getKitapAdi() + "): ");
            kitap.setKitapAdi(scanner.nextLine());

            System.out.print("Yeni ISBN (" + kitap.getIsbn() + "): ");
            kitap.setIsbn(scanner.nextLine());

            System.out.print("Basım Yılı (yyyy-MM-dd): ");
            String basimStr = scanner.nextLine();

            try {
                kitap.setBasimYili(LocalDate.parse(basimStr)); // doğru formatta girildiyse
            } catch (Exception e) {
                System.out.println("❌ Hatalı tarih formatı! Lütfen yyyy-MM-dd şeklinde girin.");
                return; // veya kullanıcıya tekrar deneme şansı verebilirsin
            }

            System.out.print("Yeni Yayınevi (" + kitap.getYayinevi() + "): ");
            kitap.setYayinevi(scanner.nextLine());

            System.out.print("Yeni Adet (" + kitap.getAdet() + "): ");
            kitap.setAdet(Integer.parseInt(scanner.nextLine()));

            kitapService.guncelle(kitap);
            System.out.println(" Kitap başarıyla güncellendi.");
        } else {
            System.out.println(" Kitap bulunamadı.");
        }
    }

    private void kitapAra() {
        System.out.print("Aranacak kitabın ISBN'sini girin: ");
        String isbn = scanner.nextLine();

        kitapService.kitapBulByIsbn(isbn).ifPresentOrElse(
                kitap -> System.out.println(" Kitap bulundu: " + kitap.getKitapAdi()),
                () -> System.out.println(" Kitap bulunamadı."));
    }

    private void aktifUyeleriListele() {
        System.out.println("\n=== Aktif Üyeler ===");
        aktifUyeIterator.reset();

        while (aktifUyeIterator.hasNext()) {
            Uye uye = aktifUyeIterator.next();
            System.out.printf("ID: %d - Ad Soyad: %s %s - Email: %s%n",
                    uye.getUyeID(),
                    uye.getUyeAdi(),
                    uye.getUyeSoyad(),
                    uye.getEmail());
        }
    }
}
