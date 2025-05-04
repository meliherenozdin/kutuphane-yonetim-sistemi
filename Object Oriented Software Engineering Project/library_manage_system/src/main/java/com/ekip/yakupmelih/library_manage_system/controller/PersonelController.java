package com.ekip.yakupmelih.library_manage_system.controller;

import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.model.Personel;
import com.ekip.yakupmelih.library_manage_system.repository.KategoriRepository;
import com.ekip.yakupmelih.library_manage_system.repository.RafRepository;
import com.ekip.yakupmelih.library_manage_system.repository.YazarRepository;
import com.ekip.yakupmelih.library_manage_system.service.KitapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

@Component
public class PersonelController implements KullaniciController.Kullanici {

    @Autowired
    private KitapService kitapService;

    @Autowired
    private KategoriRepository kategoriRepository;

    @Autowired
    private YazarRepository yazarRepository;

    @Autowired
    private RafRepository rafRepository;

    private final Scanner scanner = new Scanner(System.in);

    private Personel personel;

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
            System.out.println("5. Çıkış");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> kitapEkle();
                case "2" -> kitapSil();
                case "3" -> kitapGuncelle();
                case "4" -> kitapAra();
                case "5" -> {
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
}
